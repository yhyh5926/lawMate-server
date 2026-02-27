import { Link } from "react-router-dom";
import lawMateLogo from "../../assets/lawMateLogo.png";
import "../../styles/common/header.css";
import useAuthStore  from "../../zustand/auth_store";

const Header = () => {
  const { user } = useAuthStore();

  return (
    <header className="header">
      <nav className="header-nav">
        <div className="header-left">
          <Link to="/" className="logo-link">
            <img src={lawMateLogo} alt="LawMate Logo" className="logo-image" />
          </Link>
        </div>

        <div className="header-center">
          <Link to="/" className="nav-link">
            메인
          </Link>
          <Link to="/precedent" className="nav-link">
            판례 검색
          </Link>
          <Link to="/lawyer" className="nav-link">
            변호사
          </Link>
          <Link to="/community" className="nav-link">
            커뮤니티
          </Link>
          <Link to="/chat" className="nav-link">
            상담
          </Link>

          {/* 관리자만 보이게 */}
          {user?.id === "admin" && (
            <Link to="/admin" className="nav-link admin-link">
              관리자
            </Link>
          )}
        </div>

        <div className="header-right">
          {user ? (
            <Link to="/mypage" className="nav-link mypage-link">
              마이 페이지
            </Link>
          ) : (
            <>
            <Link to="/login" className="nav-link auth-link">
              로그인
            </Link> <Link to="/join" className="nav-link auth-link">
              회원가입
            </Link>
            </>
          )}

          
        </div>
      </nav>
    </header>
  );
};

export default Header;
