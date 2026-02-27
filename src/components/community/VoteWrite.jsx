import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../styles/community/community.css';
const VoteWrite = () => {
  const [form, setForm] = useState({ title: '', content: '', optA: '', optB: '' });
  const navigate = useNavigate();

  const handleSubmit = () => {
    console.log("투표 생성:", form);
    alert("투표가 생성되었습니다!");
    navigate('/community/vote');
  };

  return (
    <div className="container">
      <div className="form-group">
        <h2 className="title">분쟁 투표 만들기</h2>
        <input type="text" placeholder="투표 제목" onChange={e => setForm({...form, title: e.target.value})} />
        <textarea placeholder="내용 설명" onChange={e => setForm({...form, content: e.target.value})} />
        <div className="vote-option-group">
          <input type="text" placeholder="A 선택지" onChange={e => setForm({...form, optA: e.target.value})} />
          <span>VS</span>
          <input type="text" placeholder="B 선택지" onChange={e => setForm({...form, optB: e.target.value})} />
        </div>
        <button className="btn-primary" onClick={handleSubmit}>등록하기</button>
      </div>
    </div>
  );
};

export default VoteWrite;