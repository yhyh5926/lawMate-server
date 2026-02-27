require("dotenv").config();
const fs = require("fs");
const path = require("path");
const { getConnection } = require("../src/db");

async function runSqlFile(conn, filepath) {
  const sql = fs.readFileSync(filepath, "utf-8");
  // Split by "/" on a line by itself (Oracle SQL*Plus style) OR by ";"
  // We'll keep it simple: execute statements separated by ';' but
  // also handle PL/SQL blocks ended by "/".
  const lines = sql.split(/\r?\n/);

  let buffer = [];
  const statements = [];

  function pushBufferAsStatement() {
    const stmt = buffer.join("\n").trim();
    if (stmt) statements.push(stmt);
    buffer = [];
  }

  for (const line of lines) {
    // Skip comments
    if (line.trim().startsWith("--")) continue;

    if (line.trim() === "/") {
      // End of PL/SQL block
      pushBufferAsStatement();
      continue;
    }

    buffer.push(line);

    // If line contains ';' end-of-statement (not perfect but works for our scripts)
    if (line.trim().endsWith(";")) {
      pushBufferAsStatement();
    }
  }
  pushBufferAsStatement();

  for (const stmt of statements) {
    const cleaned = stmt.replace(/;\s*$/, "");
    if (!cleaned.trim()) continue;
    await conn.execute(cleaned);
  }
}

async function main() {
  let conn;
  try {
    conn = await getConnection();
    console.log("Connected to Oracle.");

    const schemaPath = path.join(__dirname, "..", "sql", "schema.sql");
    const seedPath = path.join(__dirname, "..", "sql", "seed.sql");

    console.log("Applying schema.sql ...");
    await runSqlFile(conn, schemaPath);

    console.log("Applying seed.sql ...");
    await runSqlFile(conn, seedPath);

    await conn.commit();
    console.log("✅ Done. Tables created and sample data inserted.");
  } catch (err) {
    console.error("❌ init-db failed:", err);
    process.exitCode = 1;
  } finally {
    if (conn) await conn.close().catch(() => {});
  }
}

main();
