import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../../api/auth_api';
import '../../styles/auth/Auth.css';

const JoinPage = () => {
  const navigate = useNavigate();
  const [role, setRole] = useState('USER'); // 'USER' or 'LAWYER'
  
  const [form, setForm] = useState({
    id: '', pw: '', name: '', email: '', nickname: '',
    // 변호사 전용 필드
    licenseName: '', education: '', phone: '', office: ''
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleJoin = async () => {
    // 기본 유효성 검사
    if (!form.id || !form.pw || !form.name) return alert("필수 정보를 입력하세요.");
    
    // 변호사일 경우 추가 정보 확인
    if (role === 'LAWYER') {
        if(!form.licenseName || !form.phone || !form.office) {
            return alert("변호사 정보를 모두 입력해주세요.");
        }
    }

    const userData = { ...form, role: role };
    const res = await authApi.join(userData);
    
    if (res.success) {
      alert("회원가입 완료! 로그인 해주세요.");
      navigate('/login');
    }
  };

  return (
    <div className="login-container">
      <h2 className="login-title">회원가입</h2>
      
      {/* 탭 버튼 */}
      <div className="join-tab-group">
        <button 
          onClick={() => setRole('USER')} 
          className={`join-tab-btn ${role === 'USER' ? 'active' : ''}`}
        >
          일반 회원
        </button>
        <button 
          onClick={() => setRole('LAWYER')} 
          className={`join-tab-btn ${role === 'LAWYER' ? 'active' : ''}`}
        >
          변호사 회원
        </button>
      </div>

      <div className="login-form">
        <div className="form-group">
            <label className="input-label">아이디</label>
            <input name="id" onChange={handleChange} className="login-input" placeholder="아이디를 입력하세요"/>
        </div>
        <div className="form-group">
            <label className="input-label">비밀번호</label>
            <input type="password" name="pw" onChange={handleChange} className="login-input" placeholder="비밀번호를 입력하세요"/>
        </div>
        <div className="form-group">
            <label className="input-label">이름</label>
            <input name="name" onChange={handleChange} className="login-input" placeholder="이름을 입력하세요"/>
        </div>
        <div className="form-group">
            <label className="input-label">이메일</label>
            <input name="email" onChange={handleChange} className="login-input" placeholder="이메일 (example@test.com)"/>
        </div>
        <div className="form-group">
            <label className="input-label">닉네임</label>
            <input name="nickname" onChange={handleChange} className="login-input" placeholder="사용할 닉네임을 입력하세요"/>
        </div>

        {/* 변호사 전용 추가 입력 필드 */}
        {role === 'LAWYER' && (
          <div style={{background:'#f8fafc', padding:'20px', borderRadius:'12px', border:'1px solid #e2e8f0', display:'flex', flexDirection:'column', gap:'15px'}}>
            <h4 style={{margin:'0', color:'#1e293b'}}>⚖️ 변호사 정보 입력</h4>
            
            <div className="form-group">
                <label className="input-label">자격증 명</label>
                <input name="licenseName" placeholder="예: 제 54회 사법시험 합격" onChange={handleChange} className="login-input" />
            </div>
            <div className="form-group">
                <label className="input-label">최종 학력</label>
                <input name="education" placeholder="예: 서울대학교 법학과" onChange={handleChange} className="login-input" />
            </div>
            <div className="form-group">
                <label className="input-label">전화번호</label>
                <input name="phone" placeholder="예: 010-1234-5678" onChange={handleChange} className="login-input" />
            </div>
            <div className="form-group">
                <label className="input-label">사무실 위치</label>
                <input name="office" placeholder="예: 서울시 서초구 서초대로..." onChange={handleChange} className="login-input" />
            </div>
          </div>
        )}

        <button onClick={handleJoin} className="login-btn primary">가입하기</button>
      </div>
    </div>
  );
};

export default JoinPage;