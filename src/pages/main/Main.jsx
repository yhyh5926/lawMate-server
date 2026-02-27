import { useNavigate } from "react-router-dom";
import HeroBanner from "../../components/main/HeroBanner"
import StatsChart from "../../components/main/StatsChart";
import SearchBar from "../../components/main/SearchBar";
import AiCases from "../../components/main/AiCases";
import "../../styles/main/main.css"

export default function Main() {
  const navigate = useNavigate();

  const handleWrite = () => {
    const token = localStorage.getItem("token");

    if (!token) {
      alert("로그인이 필요합니다.");
      navigate("/login");
      return;
    }

    navigate("/community/qna/write");
  };

  return (
    <div className="main-container">
      <HeroBanner onWrite={handleWrite} />

      <div className="main-content">
        <header className="main-header">
          <div className="header-text">
            <h1 className="main-title">대시보드</h1>
            <p className="main-subtitle">사건 통계 · 판례 검색 · AI 맞춤 추천</p>
          </div>
          <button className="quick-write-btn" onClick={handleWrite}>
            <span className="btn-icon">✍️</span>
            질문 등록
          </button>
        </header>

        <div className="dashboard-grid">
          {/* 사건 통계 */}
          <section className="dashboard-card stats-card">
            <div className="card-header">
              <div className="header-left">
                <span className="card-icon">📊</span>
                <h2 className="card-title">사건 통계</h2>
              </div>
              <span className="card-badge">일/주 단위</span>
            </div>
            <div className="card-body">
              <StatsChart />
            </div>
          </section>

          {/* 판례 검색 */}
          <section className="dashboard-card search-card">
            <div className="card-header">
              <div className="header-left">
                <span className="card-icon">🔍</span>
                <h2 className="card-title">판례 검색</h2>
              </div>
              <span className="card-badge">Enter 검색</span>
            </div>
            <div className="card-body">
              <SearchBar />
              <p className="card-hint">키워드를 입력하고 Enter를 누르면 검색 페이지로 이동합니다.</p>
            </div>
          </section>
        </div>


        {/* 최근 활동 */}
        <section className="dashboard-card recent-card">
          <div className="card-header">
            <div className="header-left">
              <span className="card-icon">🕘</span>
              <h2 className="card-title">최근 활동</h2>
            </div>
            <span className="card-badge">빠른 이동</span>
          </div>
          <div className="card-body recent-body">
            <div className="recent-col">
              <h3 className="recent-title">최근 본 판례</h3>
              <p className="card-hint">최근 확인한 판례를 최대 3개까지 보여줍니다.</p>
              <button className="recent-link" type="button" onClick={() => navigate("/precedent")}>
                판례 검색으로 이동 →
              </button>
            </div>
            <div className="recent-col">
              <h3 className="recent-title">최근 상담/질문</h3>
              <p className="card-hint">최근 작성/열람한 질문을 빠르게 확인하세요.</p>
              <button className="recent-link" type="button" onClick={() => navigate("/community/qna")}>
                커뮤니티로 이동 →
              </button>
            </div>
          </div>
        </section>

        {/* AI 맞춤 판례 */}
        <section id="ai-section" className="dashboard-card ai-card">
          <div className="card-header">
            <div className="header-left">
              <span className="card-icon">🤖</span>
              <h2 className="card-title">AI 맞춤 판례</h2>
            </div>
            <span className="card-badge highlight">AI 추천</span>
          </div>
          <div className="card-body">
            <AiCases />
          </div>
        </section>
      </div>
    </div>
  );
}