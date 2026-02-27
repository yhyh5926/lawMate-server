// Centralized API client (fetch-based)
// Uses Vite proxy by default (see vite.config.js). For production, set VITE_API_BASE_URL.

const BASE_URL = import.meta.env?.VITE_API_BASE_URL || "";

function buildUrl(path) {
  if (!path.startsWith("/")) path = "/" + path;
  return BASE_URL ? `${BASE_URL}${path}` : path;
}

async function request(path, { method = "GET", body, auth = false } = {}) {
  const headers = { "Content-Type": "application/json" };

  if (auth) {
    const token = localStorage.getItem("token");
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(buildUrl(path), {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined
  });

  const data = await res.json().catch(() => ({}));

  if (!res.ok || data?.ok === false) {
    // backend returns { ok:false, message }
    const msg = data?.message || data?.error || `Request failed (${res.status})`;
    throw msg;
  }

  return data;
}

export const apiClient = { request };
