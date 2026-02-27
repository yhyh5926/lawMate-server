require("dotenv").config();
const express = require("express");
const cors = require("cors");
const { getConnection, oracledb } = require("./db");
const { toInt } = require("./utils");

const app = express();
app.use(express.json({ limit: "1mb" }));

// CORS (for Vite dev server)
app.use(
  cors({
    origin: process.env.CORS_ORIGIN || true,
    credentials: true,
  })
);

app.get("/health", (req, res) => {
  res.json({ ok: true, time: new Date().toISOString() });
});

/**
 * GET /api/stats?groupBy=day|week
 * Response: { labels: string[], counts: number[] }
 */
app.get("/api/stats", async (req, res) => {
  const groupBy = req.query.groupBy === "week" ? "week" : "day";

  const sqlDay = `
    SELECT TO_CHAR(TRUNC(created_at), 'MM-DD') AS label,
           COUNT(*) AS cnt
    FROM cases
    WHERE created_at >= TRUNC(SYSDATE) - 6
    GROUP BY TRUNC(created_at)
    ORDER BY TRUNC(created_at)
  `;

  const sqlWeek = `
    SELECT TO_CHAR(created_at, 'IYYY-"W"IW') AS label,
           COUNT(*) AS cnt
    FROM cases
    WHERE created_at >= TRUNC(SYSDATE) - 28
    GROUP BY TO_CHAR(created_at, 'IYYY-"W"IW')
    ORDER BY MIN(created_at)
  `;

  const sql = groupBy === "week" ? sqlWeek : sqlDay;

  let conn;
  try {
    conn = await getConnection();
    const result = await conn.execute(sql);

    const labels = result.rows.map((r) => r.LABEL);
    const counts = result.rows.map((r) => Number(r.CNT));

    res.json({ labels, counts });
  } catch (err) {
    console.error("stats error:", err);
    res.status(500).json({ ok: false, message: "Failed to load stats" });
  } finally {
    if (conn) await conn.close().catch(() => {});
  }
});

/**
 * GET /api/precedents?keyword=&page=1&pageSize=10
 * Response: { ok:true, page, pageSize, total, items:[...] }
 */
app.get("/api/precedents", async (req, res) => {
  const keyword = (req.query.keyword || "").toString().trim();
  const page = Math.max(1, toInt(req.query.page, 1));
  const pageSize = Math.min(50, Math.max(1, toInt(req.query.pageSize, 10)));
  const offset = (page - 1) * pageSize;

  // Simple search: title or keywords contains keyword (case-insensitive)
  const where = keyword
    ? `WHERE LOWER(title) LIKE '%' || LOWER(:kw) || '%'
        OR LOWER(keywords) LIKE '%' || LOWER(:kw) || '%'`
    : "";

  const sqlItems = `
    SELECT id, category, title,
           DBMS_LOB.SUBSTR(summary, 400, 1) AS summary_preview,
           keywords,
           decided_at
    FROM precedents
    ${where}
    ORDER BY decided_at DESC NULLS LAST, created_at DESC
    OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY
  `;

  const sqlCount = `
    SELECT COUNT(*) AS total
    FROM precedents
    ${where}
  `;

  let conn;
  try {
    conn = await getConnection();

    const bindsBase = keyword ? { kw: keyword } : {};
    const countRes = await conn.execute(sqlCount, bindsBase);
    const total = Number(countRes.rows?.[0]?.TOTAL || 0);

    const itemsRes = await conn.execute(
      sqlItems,
      { ...bindsBase, offset, pageSize },
      { outFormat: oracledb.OUT_FORMAT_OBJECT }
    );

    const items = itemsRes.rows.map((r) => ({
      id: r.ID,
      category: r.CATEGORY,
      title: r.TITLE,
      summary: r.SUMMARY_PREVIEW,
      keywords: r.KEYWORDS,
      decidedAt: r.DECIDED_AT,
    }));

    res.json({ ok: true, page, pageSize, total, items });
  } catch (err) {
    console.error("precedents error:", err);
    res.status(500).json({ ok: false, message: "Failed to load precedents" });
  } finally {
    if (conn) await conn.close().catch(() => {});
  }
});

/**
 * POST /api/ai/recommend
 * Very simple rule-based recommendation (fallback). Replace with real AI later.
 * Body: { caseType?: string, description?: string }
 * Response: { ok:true, recommendations:[{rank, tag, title, description, keyword}] }
 */
app.post("/api/ai/recommend", async (req, res) => {
  const caseType = (req.body?.caseType || "").toString().trim();
  const description = (req.body?.description || "").toString().trim();

  // If user gave a caseType, bias results by keyword search in precedents
  const keyword = caseType || (description.split(/\s+/).slice(0, 2).join(" ") || "").trim();

  const fallback = [
    {
      rank: 1,
      tag: "부동산",
      title: "임대차 분쟁",
      description: "계약 해지/보증금 반환 관련 핵심 쟁점과 최근 판례를 요약합니다.",
      keyword: "임대차 분쟁",
    },
    {
      rank: 2,
      tag: "손해배상",
      title: "교통사고 손해배상",
      description: "과실비율 및 치료비/위자료 산정 기준을 참고할 수 있습니다.",
      keyword: "교통사고 손해배상",
    },
    {
      rank: 3,
      tag: "형사",
      title: "명예훼손",
      description: "사실 적시/의견표현 구분과 위법성 조각 사유를 확인합니다.",
      keyword: "명예훼손",
    },
  ];

  // Optional: try to pull top 3 precedents matching keyword
  let conn;
  try {
    conn = await getConnection();

    const sql = `
      SELECT id, category, title,
             DBMS_LOB.SUBSTR(summary, 200, 1) AS summary_preview
      FROM precedents
      WHERE LOWER(title) LIKE '%' || LOWER(:kw) || '%'
         OR LOWER(keywords) LIKE '%' || LOWER(:kw) || '%'
      ORDER BY decided_at DESC NULLS LAST, created_at DESC
      FETCH FIRST 3 ROWS ONLY
    `;

    const kw = keyword || "분쟁";
    const result = await conn.execute(sql, { kw });

    if (result.rows && result.rows.length > 0) {
      const recommendations = result.rows.map((r, idx) => ({
        rank: idx + 1,
        tag: r.CATEGORY || "추천",
        title: r.TITLE,
        description: r.SUMMARY_PREVIEW || "관련 판례 요약을 확인하세요.",
        keyword: kw,
        precedentId: r.ID,
      }));
      return res.json({ ok: true, recommendations, source: "oracle" });
    }

    return res.json({ ok: true, recommendations: fallback, source: "fallback" });
  } catch (err) {
    console.error("ai recommend error:", err);
    return res.json({ ok: true, recommendations: fallback, source: "fallback" });
  } finally {
    if (conn) await conn.close().catch(() => {});
  }
});

const port = Number(process.env.PORT || 4000);
app.listen(port, () => {
  console.log(`LawMate Oracle backend running on http://localhost:${port}`);
});
