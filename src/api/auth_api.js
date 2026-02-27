import { apiClient } from "./apiClient";

export const authApi = {
  // [로그인]
  login: async (id, password) => {
    const data = await apiClient.request("/api/auth/login", {
      method: "POST",
      body: { id, password }
    });
    // backend: { ok:true, user:{... , token} }
    return data.user;
  },

  // [회원가입]
  join: async (userData) => {
    const data = await apiClient.request("/api/auth/join", {
      method: "POST",
      body: userData
    });
    // backend: { ok:true, message }
    return { success: true, message: data.message };
  },

  // [아이디 찾기]
  findId: async (email) => {
    const data = await apiClient.request("/api/auth/find-id", {
      method: "POST",
      body: { email }
    });
    // backend: { ok:true, id }
    return data.id;
  },

  // [비밀번호 찾기]
  findPw: async (id, email) => {
    const data = await apiClient.request("/api/auth/find-pw", {
      method: "POST",
      body: { id, email }
    });
    // backend: { ok:true, pw }
    return data.pw;
  },

  // [회원 정보 수정]
  updateUser: async (updatedData) => {
    if (!updatedData?.id) throw "id is required";
    const data = await apiClient.request(`/api/users/${updatedData.id}`, {
      method: "PUT",
      body: updatedData,
      auth: true
    });
    return data.user;
  },

  // --- 관리자 기능 ---

  // 승인 대기 변호사 조회
  getPendingLawyers: async () => {
    const data = await apiClient.request("/api/admin/pending-lawyers", { auth: true });
    return data.items || [];
  },

  // 변호사 승인
  approveLawyer: async (userId) => {
    const data = await apiClient.request("/api/admin/approve-lawyer", {
      method: "POST",
      body: { userId },
      auth: true
    });
    return data.ok === true;
  },

  // 신고 목록 조회
  getReports: async () => {
    const data = await apiClient.request("/api/admin/reports", { auth: true });
    return data.items || [];
  },

  // 유저 정지
  banUser: async (targetId) => {
    const data = await apiClient.request("/api/admin/ban-user", {
      method: "POST",
      body: { targetId },
      auth: true
    });
    return data.ok === true;
  },

  // 회원 탈퇴 (Soft Delete)
  leaveUser: async (userId) => {
    const data = await apiClient.request(`/api/users/${userId}/leave`, {
      method: "POST",
      auth: true
    });
    return data.ok === true;
  }
};
