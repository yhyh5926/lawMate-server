import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Skeleton from "./Skeleton";

export default function AiCases() {
  const navigate = useNavigate();
  const [cases, setCases] = useState(null);
  const [info, setInfo] = useState(null);

  useEffect(() => {
    let cancelled = false;

    const fallback = () => [
      {
        title: "임대차 분쟁",
        summary: "계약 해지/보증금 반환 관련 핵심 쟁점과 최근 판례를 요약합니다.",
        tag: "부동산"
      },
      {
        title: "교통사고 손해배상",
        summary: "과실비율 및 치료비/위자료 산정 기준을 참고할 수 있습니다.",
        tag: "손해배상"
      },
      {
        title: "명예훼손",
        summary: "사실 적시/의견표현 구분과 위법성 조각 사유를 확인합니다.",
        tag: "형사"
      }
    ];

    fetch("/api/ai/recommend")
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((data) => {
        if (cancelled) return;
        if (!Array.isArray(data)) throw new Error("Invalid payload");
        setCases(data);
      })
      .catch(() => {
        if (cancelled) return;
        setInfo("AI 추천 기능을 준비 중입니다. 아래는 예시 추천입니다.");
        setCases(fallback());
      });

    return () => {
      cancelled = true;
    };
  }, []);

  const goDetail = (c) => {
    // 판례 검색 페이지로 이동 + 키워드 전달
    const keyword = c?.title || c?.tag || "";
    navigate(`/precedent?keyword=${encodeURIComponent(keyword)}`);
  };

  if (!cases) return <Skeleton />;

  if (Array.isArray(cases) && cases.length === 0) {
    return (
      <div className="ai-cases-container">
        <p className="ai-error-message">추천 결과가 없습니다. 다른 키워드로 다시 시도해 주세요.</p>
        <button className="case-detail-btn" type="button" onClick={() => navigate("/precedent")}>
          판례 검색으로 이동
        </button>
      </div>
    );
  }

  return (
    <div className="ai-cases-container">
      {info && <p className="ai-error-message">{info}</p>}

      <div className="ai-cases-grid">
        {cases.map((c, i) => (
          <div key={i} className="ai-case-card">
            <div className="case-card-header">
              <span className="case-number">#{i + 1}</span>
              {c.tag && <span className="case-tag">{c.tag}</span>}
            </div>
            <h3 className="case-title">{c.title}</h3>
            <p className="case-summary">{c.summary}</p>
            <button className="case-detail-btn" type="button" onClick={() => goDetail(c)}>
              자세히 보기
              <svg className="arrow-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path
                  d="M6 12L10 8L6 4"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
