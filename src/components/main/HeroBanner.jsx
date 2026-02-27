import { useNavigate } from "react-router-dom";

export default function HeroBanner({ onWrite }) {
  const navigate = useNavigate();

  const scrollToAI = () => {
    const el = document.getElementById("ai-section");
    if (el) el.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  const goPrecedent = () => navigate("/precedent");
  const goConsult = () => navigate("/lawyer");

  return (
    <section className="hero" aria-label="SmartLaw hero">
      <div className="heroInner">
        <div className="heroLeft">
          <div className="heroPills">
            <span
              className="pill"
              role="button"
              tabIndex={0}
              onClick={scrollToAI}
              onKeyDown={(e) => e.key === "Enter" && scrollToAI()}
              style={{ cursor: "pointer" }}
            >
              AI 맞춤 추천
            </span>

            <span
              className="pill"
              role="button"
              tabIndex={0}
              onClick={goPrecedent}
              onKeyDown={(e) => e.key === "Enter" && goPrecedent()}
              style={{ cursor: "pointer" }}
            >
              판례 검색
            </span>

            <span
              className="pill"
              role="button"
              tabIndex={0}
              onClick={goConsult}
              onKeyDown={(e) => e.key === "Enter" && goConsult()}
              style={{ cursor: "pointer" }}
            >
              법률 상담
            </span>
          </div>

          <h1 className="heroTitle">AI 기반 스마트 법률 플랫폼</h1>
          <p className="heroText">
            사건 통계부터 판례 검색, AI 추천까지 한 번에.
            <br />
            필요한 정보를 빠르게 찾고 변호사 상담 또는 질문 글도 바로 작성하세요.
          </p>

          <div className="heroActions">
            <button className="btn btnPrimary" type="button" onClick={goPrecedent}>
              판례 검색하기
            </button>

            <button className="btn btnGhost" type="button" onClick={goConsult}>
              법률 상담하기
            </button>

            <button
              className="btn btnGhost"
              type="button"
              onClick={() => (onWrite ? onWrite() : navigate("/community/qna/write"))}
            >
              질문 글쓰기
            </button>
          </div>
        </div>

        {/* 통계/요약 영역 (현재는 샘플) */}
        <div className="heroRight">
          <div className="heroStat" title="샘플 수치">
            <div className="heroStatLabel">오늘의 사건 · 샘플</div>
            <div className="heroStatValue">+128</div>
          </div>
          <div className="heroStat" title="샘플 수치">
            <div className="heroStatLabel">이번 주 판례 · 샘플</div>
            <div className="heroStatValue">3,420</div>
          </div>

          <div
            className="heroStat"
            role="button"
            tabIndex={0}
            title="AI 추천 영역으로 이동"
            onClick={scrollToAI}
            onKeyDown={(e) => e.key === "Enter" && scrollToAI()}
            style={{ cursor: "pointer" }}
          >
            <div className="heroStatLabel">AI 추천</div>
            <div className="heroStatValue">바로 보기</div>
          </div>
        </div>
      </div>
    </section>
  );
}
