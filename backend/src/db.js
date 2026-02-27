const oracledb = require("oracledb");

// Return rows as JS objects: { COL: value }
oracledb.outFormat = oracledb.OUT_FORMAT_OBJECT;

// If you use Oracle Instant Client, ensure LD_LIBRARY_PATH is set on Linux.
// Example: export LD_LIBRARY_PATH=/opt/oracle/instantclient_21_13:$LD_LIBRARY_PATH

async function getConnection() {
  return oracledb.getConnection({
    user: process.env.ORACLE_USER,
    password: process.env.ORACLE_PASSWORD,
    connectString: process.env.ORACLE_CONNECT_STRING,
  });
}

module.exports = { getConnection, oracledb };
