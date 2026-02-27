import React, { useState } from 'react';
import '../../styles/community/community.css';
const QnaWrite = () => {
  const [form, setForm] = useState({ title: '', content: '' });

  const handleSubmit = async () => {
    // 기존에 짰던 Spring/Flask 통신 로직 유지
    console.log("데이터 전송:", form);
    alert("질문이 등록되었습니다.");
  };

  return (
    <div className="container">
      <div className="form-group">
        <h2 className="title">법률 질문 등록</h2>
        <input type="text" placeholder="제목" onChange={e => setForm({...form, title: e.target.value})} />
        <textarea placeholder="상담 내용을 자세히 적어주세요" onChange={e => setForm({...form, content: e.target.value})} />
        <button className="btn-primary" onClick={handleSubmit}>AI 분석 및 등록</button>
      </div>
    </div>
  );
};

export default QnaWrite;