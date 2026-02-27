import { Bar } from "react-chartjs-2";
import { useEffect, useMemo, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend
} from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend);

const GROUP_OPTIONS = [
  { key: "day", label: "일별" },
  { key: "week", label: "주별" }
];

export default function StatsChart() {
  const [groupBy, setGroupBy] = useState("day");
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  // ✅ 샘플 데이터 (API 연결 전 fallback)
  const sample = useMemo(() => {
    const daily = {
      labels: ["02-07", "02-08", "02-09", "02-10", "02-11", "02-12", "02-13"],
      datasets: [{ label: "사건 수(샘플/일별)", data: [4, 7, 2, 6, 3, 5, 8] }]
    };
    const weekly = {
      labels: ["W05", "W06", "W07", "W08"],
      datasets: [{ label: "사건 수(샘플/주별)", data: [22, 31, 18, 27] }]
    };
    return { day: daily, week: weekly };
  }, []);

  // ✅ Chart.js 옵션 (더 깔끔하고 대시보드 느낌)
  const options = useMemo(
    () => ({
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          displayColors: false,
          callbacks: {
            label: (ctx) => ` ${ctx.parsed.y}건`
          }
        }
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: { maxRotation: 0 }
        },
        y: {
          beginAtZero: true,
          grid: { drawBorder: false },
          ticks: { callback: (v) => `${v}` }
        }
      }
    }),
    []
  );

  useEffect(() => {
    let cancelled = false;

    setData(null);
    setError(null);

    fetch(`/api/stats?groupBy=${encodeURIComponent(groupBy)}`)
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
      })
      .then((result) => {
        if (cancelled) return;

        // expected payload: { labels: string[], counts: number[] }
        if (!result?.labels || !result?.counts) throw new Error("Invalid payload");

        setData({
          labels: result.labels,
          datasets: [
            {
              label: groupBy === "day" ? "사건 수(일별)" : "사건 수(주별)",
              data: result.counts
            }
          ]
        });
      })
      .catch(() => {
        if (cancelled) return;
        setError("통계 API 연결 전입니다. 샘플 데이터를 표시합니다.");
        setData(sample[groupBy]);
      });

    return () => {
      cancelled = true;
    };
  }, [groupBy, sample]);

  // ✅ KPI 요약 (최근/이전/증감)
  const summary = useMemo(() => {
    const arr = data?.datasets?.[0]?.data;
    if (!arr || arr.length === 0) return null;

    const last = Number(arr[arr.length - 1] ?? 0);
    const prev = Number(arr[arr.length - 2] ?? 0);
    const delta = prev === 0 ? null : Math.round(((last - prev) / prev) * 100);

    return { last, prev, delta };
  }, [data]);

  return (
    <div>
      {/* ✅ 일별/주별 선택 버튼 */}
      <div className="segmented" role="tablist" aria-label="통계 단위 선택">
        {GROUP_OPTIONS.map((opt) => (
          <button
            key={opt.key}
            type="button"
            className={`segBtn ${groupBy === opt.key ? "isActive" : ""}`}
            onClick={() => setGroupBy(opt.key)}
            role="tab"
            aria-selected={groupBy === opt.key}
          >
            {opt.label}
          </button>
        ))}
      </div>

      {/* ✅ API fallback 메시지 */}
      {error ? <p className="helperText">{error}</p> : null}

      {/* ✅ KPI Summary */}
      {summary && (
        <div className="statsSummary">
          <div className="kpi">
            <span className="kpiLabel">최근</span>
            <span className="kpiValue">{summary.last}건</span>
          </div>

          <div className="kpi">
            <span className="kpiLabel">이전</span>
            <span className="kpiValue">{summary.prev}건</span>
          </div>

          <div className="kpi">
            <span className="kpiLabel">증감</span>
            <span
              className={`kpiValue ${
                summary.delta === null ? "" : summary.delta >= 0 ? "pos" : "neg"
              }`}
            >
              {summary.delta === null ? "-" : `${summary.delta}%`}
            </span>
          </div>
        </div>
      )}

      {/* ✅ Chart */}
      <div style={{ height: 240 }}>
        {!data ? <p className="helperText">로딩 중...</p> : <Bar data={data} options={options} />}
      </div>

      {/* ✅ DEV 모드에서만 표시 */}
      {import.meta.env.DEV && (
        <p className="helperText">
          DEV: <code>fetch('/api/stats?groupBy=day|week')</code> → Chart.js 바인딩
        </p>
      )}
    </div>
  );
}
