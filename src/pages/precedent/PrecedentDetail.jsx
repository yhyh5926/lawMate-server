import { useParams, useNavigate } from "react-router-dom";
import { mockPrecedents } from "../../mocks/precedent/mockPrecedents";
import { useState } from "react";
import "../../styles/precedent/precedentDetail.css";

export default function PrecedentDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const precedent = mockPrecedents.find((p) => p.id === id);

  const [showLegalPrinciple, setShowLegalPrinciple] = useState(false);

  if (!precedent) {
    return (
      <div className="not-found-container">
        <div className="not-found-content">
          <div className="not-found-icon">⚠️</div>
          <h2 className="not-found-title">판례를 찾을 수 없습니다</h2>
          <p className="not-found-text">
            요청하신 판례가 존재하지 않거나 삭제되었습니다.
          </p>
          <button className="back-button" onClick={() => navigate(-1)}>
            돌아가기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="detail-container">
      <div className="detail-wrapper">
        {/* 상단 네비게이션 */}
        <div className="detail-nav">
          <button className="nav-back-button" onClick={() => navigate(-1)}>
            ← 목록으로
          </button>
          <div className="nav-badges">
            <span className="detail-category-badge">{precedent.category}</span>
            <span className="detail-case-number">{precedent.caseNumber}</span>
          </div>
        </div>

        {/* 헤더 */}
        <header className="detail-header">
          <h1 className="detail-title">{precedent.title}</h1>
          <div className="detail-meta-info">
            <span className="meta-info-item">
              <span className="meta-icon">⚖️</span>
              {precedent.court}
            </span>
            <span className="meta-divider">|</span>
            <span className="meta-info-item">
              <span className="meta-icon">📅</span>
              {precedent.date}
            </span>
            <span className="meta-divider">|</span>
            <span className="meta-info-item">
              <span className="meta-icon">📋</span>
              {precedent.result}
            </span>
          </div>
          <div className="detail-keywords">
            {precedent.keywords.map((k, i) => (
              <span key={i} className="detail-keyword-tag">
                #{k}
              </span>
            ))}
          </div>
        </header>

        {/* 한줄 요약 섹션 */}
        <section className="detail-summary-section">
          <div className="summary-icon">💡</div>
          <div className="summary-content">
            <h3 className="summary-label">한 줄 요약</h3>
            <p className="summary-text">{precedent.oneLineSummary}</p>
          </div>
        </section>

        {/* 메인 컨텐츠 */}
        <div className="detail-content">
          <section className="content-section">
            <div className="section-header">
              <span className="section-icon">📌</span>
              <h2 className="section-title">쟁점</h2>
            </div>
            <div className="section-body">
              <p className="section-text">{precedent.issue}</p>
            </div>
          </section>

          <section className="content-section">
            <div className="section-header">
              <span className="section-icon">⚖️</span>
              <h2 className="section-title">판결 내용</h2>
            </div>
            <div className="section-body">
              <p className="section-text">{precedent.decision}</p>
            </div>
          </section>
        </div>

        {/* 법리 보기 */}
        <div className="legal-principle-section">
          <button
            className={`legal-principle-toggle-button ${showLegalPrinciple ? "active" : ""}`}
            onClick={() => setShowLegalPrinciple(!showLegalPrinciple)}
          >
            <span className="button-icon">
              {showLegalPrinciple ? "📖" : "⚖️"}
            </span>
            <span className="button-text">
              {showLegalPrinciple ? "법리 닫기" : "법리 원칙 보기"}
            </span>
            <span className="button-arrow">
              {showLegalPrinciple ? "▲" : "▼"}
            </span>
          </button>

          {showLegalPrinciple && (
            <div className="legal-principle-content">
              <div className="legal-principle-header">
                <span className="legal-principle-icon">⚖️</span>
                <h3 className="legal-principle-title">법리 원칙</h3>
              </div>
              <div className="legal-principle-body">
                {precedent.legalPrinciple}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
