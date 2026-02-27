import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../styles/community/community.css'

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="container" style={{ textAlign: 'center', marginTop: '100px' }}>
      <h1 className="title" style={{ fontSize: '32px', marginBottom: '10px' }}>
        ⚖️ 법률 커뮤니티
      </h1>
      <p style={{ color: '#64748b', marginBottom: '40px' }}>
        전문가 답변부터 시민들의 의견까지, 법률 고민을 해결해보세요.
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
        {/* Q&A 카드 */}
        <div className="card-item" onClick={() => navigate('/community/qna')} style={{ padding: '40px' }}>
          <div style={{ fontSize: '40px', marginBottom: '20px' }}>💬</div>
          <h2 className="title">법률 상담 Q&A</h2>
          <p className="card-info">변호사에게 직접 질문하고<br/>전문적인 답변을 받아보세요.</p>
        </div>

        {/* 투표 카드 */}
        <div className="card-item" onClick={() => navigate('/community/vote')} style={{ padding: '40px' }}>
          <div style={{ fontSize: '40px', marginBottom: '20px' }}>📊</div>
          <h2 className="title">분쟁 투표</h2>
          <p className="card-info">일상 속 억울한 분쟁,<br/>누구의 잘못인지 투표해보세요.</p>
        </div>
      </div>
    </div>
  );
};

export default Home;