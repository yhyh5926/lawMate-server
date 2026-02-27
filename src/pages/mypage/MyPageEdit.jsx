import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuthStore from '../../zustand/auth_store';
import { authApi } from '../../api/auth_api';
import '../../styles/auth/Auth.css'; // 폼 스타일 재사용

const MyPageEdit = () => {
  const { user, updateUser } = useAuthStore();
  const navigate = useNavigate();

  // 입력 폼 상태 관리
  const [form, setForm] = useState({
    password: '', confirmPassword: '', nickname: '', 
    education: '', phone: '', office: '' // 변호사 전용 필드 추가
  });

  // 초기 데이터 로드
  useEffect(() => {
    if (user) {
      setForm({
        password: user.password,
        confirmPassword: user.password,
        nickname: user.nickname,
        // 변호사일 경우에만 데이터 채움
        education: user.role === 'LAWYER' ? user.education : '',
        phone: user.role === 'LAWYER' ? (user.phone || '') : '',
        office: user.role === 'LAWYER' ? (user.office || '') : '',
      });
    } else {
      navigate('/login');
    }
  }, [user, navigate]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
      return alert("비밀번호가 일치하지 않습니다.");
    }

    try {
      const updateData = {
        id: user.id,
        password: form.password,
        nickname: form.nickname,
        // 변호사 데이터 병합
        ...(user.role === 'LAWYER' && { 
            education: form.education,
            phone: form.phone,
            office: form.office
        })
      };

      const updatedUser = await authApi.updateUser(updateData);
      updateUser(updatedUser); // Store 업데이트
      
      alert("정보가 수정되었습니다.");
      navigate('/mypage');

    } catch (error) {
      alert("수정 실패: " + error);
    }
  };

  if (!user) return null;

  return (
    <div className="login-container">
      <h2 className="login-title">내 정보 수정</h2>
      
      <form className="login-form" onSubmit={handleSubmit}>
        
        <div className="form-group">
          <label className="input-label">아이디 (수정불가)</label>
          <input className="login-input" value={user.id} disabled style={{backgroundColor:'#f1f5f9', color:'#94a3b8'}} />
        </div>

        <div className="form-group">
          <label className="input-label">닉네임</label>
          <input name="nickname" className="login-input" value={form.nickname} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label className="input-label">비밀번호</label>
          <input type="password" name="password" className="login-input" value={form.password} onChange={handleChange} />
        </div>

        <div className="form-group">
          <label className="input-label">비밀번호 확인</label>
          <input type="password" name="confirmPassword" className="login-input" value={form.confirmPassword} onChange={handleChange} />
        </div>

        {/* 변호사 전용 수정 필드 */}
        {user.role === 'LAWYER' && (
          <div style={{marginTop:'10px', padding:'20px', backgroundColor:'#f8fafc', borderRadius:'12px', border:'1px solid #e2e8f0', display:'flex', flexDirection:'column', gap:'15px'}}>
            <h4 style={{margin:'0', fontSize:'16px', color:'#1e293b'}}>⚖️ 변호사 정보 수정</h4>
            
            <div className="form-group">
                <label className="input-label">최종 학력</label>
                <input name="education" className="login-input" value={form.education} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label className="input-label">전화번호</label>
                <input name="phone" className="login-input" value={form.phone} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label className="input-label">사무실 위치</label>
                <input name="office" className="login-input" value={form.office} onChange={handleChange} />
            </div>
            
            <p style={{fontSize:'13px', color:'#ef4444', margin:'0'}}>
              * 자격증명은 관리자 승인 사항이므로 수정할 수 없습니다.
            </p>
          </div>
        )}

        <div style={{display:'flex', gap:'10px', marginTop:'20px'}}>
            <button type="submit" className="login-btn primary">수정 완료</button>
            <button type="button" onClick={() => navigate('/mypage')} className="login-btn secondary">취소</button>
        </div>

      </form>
    </div>
  );
};

export default MyPageEdit;