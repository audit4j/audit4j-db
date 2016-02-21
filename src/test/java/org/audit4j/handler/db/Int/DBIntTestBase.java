package org.audit4j.handler.db.Int;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.audit4j.core.exception.InitializationException;
import org.hsqldb.Server;

/**
 * The Class DBIntTestBase.
 */
public class DBIntTestBase {

    /** The hsql server. */
    private Server hsqlServer;

    /**
     * Initiate db server.
     */
    protected void initiateDBServer() {
        hsqlServer = new Server();
        hsqlServer.setLogWriter(null);
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, "audit4j");
        hsqlServer.setDatabasePath(0,
                "file:audit4jdb;user=audit4jdbuser;password=audit4jdbpassword");
        hsqlServer.start();
    }

    /**
     * Shutdown db server.
     */
    protected void shutdownDBServer() {
        hsqlServer.shutdown();
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:audit4jdb", "audit4jdbuser",
                    "audit4jdbpassword");
        } catch (ClassNotFoundException e) {
            throw new InitializationException("Could not find the driver class", e);
        } catch (SQLException e) {
            throw new InitializationException("Could not initialize the connection", e);
        }
        return connection;
    }

    /**
     * Gets the audit table record count.
     *
     * @return the audit table record count
     */
    public int getAuditTableRecordCount() {
        int count = 0;
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "SELECT COUNT(*) as total FROM audit";

                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new InitializationException("SQL Exception", e);
        }
        return count;

    }

    /**
     * Gets the table record count.
     *
     * @param table
     *            the table
     * @return the table record count
     */
    public int getTableRecordCount(String table) {
        int count = 0;
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "SELECT COUNT(*) as total FROM " + table;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new InitializationException("SQL Exception", e);
        }
        return count;

    }

    /**
     * Clear table.
     *
     * @param table
     *            the table
     * @return the int
     */
    public int clearTable(String table) {
        int count = 0;
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "SELECT COUNT(*) as total FROM " + table;
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new InitializationException("SQL Exception", e);
        }
        return count;
    }

    /**
     * Gets the table list.
     *
     * @return the table list
     */
    public List<String> getTableList() {
        List<String> tables = new ArrayList<>();
        try (Connection conn = getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * Crete table if not exists.
     *
     * @param tableName
     *            the table name
     * @return true, if successful
     */
    public boolean creteTableIfNotExists(String tableName) {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (c CHAR(20))";
                stmt.executeQuery(sql);
            }
        } catch (SQLException e) {
            throw new InitializationException("SQL Exception", e);
        }
        return true;
    }

    /**
     * Drop table if exists.
     *
     * @param tableName
     *            the table name
     * @return true, if successful
     */
    public boolean dropTableIfExists(String tableName) {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                String sql = "DROP TABLE IF EXISTS tableName";
                stmt.executeQuery(sql);
            }
        } catch (SQLException e) {
            throw new InitializationException("SQL Exception", e);
        }
        return true;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        DBIntTestBase base = new DBIntTestBase();
        base.initiateDBServer();
        base.creteTableIfNotExists("aaa");
        base.dropTableIfExists("aaa");

        base.shutdownDBServer();
    }
}
