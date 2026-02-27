# LawMate Oracle Backend (Main Page APIs)

This backend is designed to support the **Main page** of your LawMate (Vite/React) project.

## What it provides
- `GET /health`
- `GET /api/stats?groupBy=day|week`  (for StatsChart)
- `GET /api/precedents?keyword=...&page=1&pageSize=10` (for precedent search/list)
- `POST /api/ai/recommend` (simple rule-based fallback AI recommendations; can be replaced with real AI later)

## Requirements
- Oracle Database running on your **Compute VM**
- Node.js 18+ recommended
- Oracle Instant Client libraries available on the VM (required by `oracledb`)
  - On many Linux distros, you can install `oracle-instantclient` packages, or use Oracle's Instant Client zip.

## 1) Configure environment
Copy `.env.example` to `.env` and fill values.

## 2) Create tables + seed data (Oracle)
Run:
```bash
npm install
npm run init-db
```

## 3) Run backend
```bash
npm run dev
# or
npm start
```

Backend default:
- http://localhost:4000

## 4) Vite frontend connection
In your Vite project, create `frontend/.env` (or project root `.env`) with:
```env
VITE_API_BASE_URL=http://localhost:4000
```

If you use a Vite proxy instead, you can keep `fetch('/api/...')` and proxy `/api` to `http://localhost:4000`.

## Notes
- The SQL scripts are in `sql/`
- The init script runs `schema.sql` then `seed.sql` in order.
