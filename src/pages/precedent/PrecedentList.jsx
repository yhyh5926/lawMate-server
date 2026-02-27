import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { mockPrecedents } from "../../mocks/precedent/mockPrecedents";
import "../../styles/precedent/precedentList.css";

export default function PrecedentList() {
  const [query, setQuery] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const keyword = params.get("keyword");
    if (keyword) setQuery(keyword);
  }, [location.search]);

  const filtered = mockPrecedents.filter(
    (p) =>
      p.title.includes(query) ||
      p.oneLineSummary.includes(query) ||
      p.issue.includes(query) ||
      p.keywords.join("").includes(query) ||
      p.caseNumber.includes(query),
  );

  const handleCardClick = (id) => {
    navigate(`/precedent/${id}`);
  };

  return (
    <div className="precedent-container">
      <div className="precedent-header">
        <h1 className="precedent-title">판례 검색</h1>
        <p className="precedent-subtitle">
          다양한 판례를 검색하고 법률 정보를 확인하세요
        </p>
      </div>

      <div className="search-section">
        <input
          type="text"
          placeholder="판례명, 사건번호, 쟁점, 키워드로 검색해보세요"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="precedent-search-input"
        />
        <div className="search-icon">🔍</div>
      </div>

      <div className="result-info">
        <span className="result-count">총 {filtered.length}건의 판례</span>
      </div>

      <div className="precedent-list">
        {filtered.map((p) => (
          <div
            key={p.id}
            className="precedent-card"
            onClick={() => handleCardClick(p.id)}
          >
            <div className="card-header">
              <div className="card-header-top">
                <span className="card-category">{p.category}</span>
                <span className="card-case-number">{p.caseNumber}</span>
              </div>
              <h3 className="card-title">{p.title}</h3>
              <span className="card-arrow">→</span>
            </div>

            <div className="card-meta">
              <span className="meta-item">
                <span className="meta-icon">⚖️</span>
                {p.court}
              </span>
              <span className="meta-divider">|</span>
              <span className="meta-item">
                <span className="meta-icon">📅</span>
                {p.date}
              </span>
            </div>

            <div className="card-summary">
              <div className="summary-label">한줄요약</div>
              <p className="summary-text">{p.oneLineSummary}</p>
            </div>

            <div className="card-content">
              <div className="content-row">
                <span className="content-label">쟁점</span>
                <p className="content-text">{p.issue}</p>
              </div>

              <div className="content-row">
                <span className="content-label">판결</span>
                <p className="content-text">{p.result}</p>
              </div>
            </div>

            <div className="card-keywords">
              {p.keywords.map((k, i) => (
                <span key={i} className="keyword-tag">
                  #{k}
                </span>
              ))}
            </div>
          </div>
        ))}
      </div>

      {filtered.length === 0 && (
        <div className="no-results">
          <div className="no-results-icon">📋</div>
          <p className="no-results-text">검색 결과가 없습니다</p>
          <p className="no-results-hint">다른 키워드로 검색해보세요</p>
        </div>
      )}
    </div>
  );
}
