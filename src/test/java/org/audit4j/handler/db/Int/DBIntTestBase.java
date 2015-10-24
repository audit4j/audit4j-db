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

public class DBIntTestBase {

    public int getAuditTableRecordCount(){
        Connection connection = null;
        Statement stmt = null;
        int count = 0;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:audit4jdb", "audit4jdbuser", "audit4jdbpassword");
            
            stmt = connection.createStatement();

            String sql = "SELECT COUNT(*) as total FROM audit";
            
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new InitializationException("Could not initialize the connection", e);
        } catch (ClassNotFoundException e) {
            throw new InitializationException("Could not find the driver class", e);
        }
        
        return count;
        
    }
    
    public List<String> getTableList(){
        Connection connection = null;
        Statement stmt = null;
        List<String> tables = new ArrayList<>();
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            connection = DriverManager.getConnection("jdbc:hsqldb:file:audit4jdb", "audit4jdbuser", "audit4jdbpassword");

            
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
          tables.add(rs.getString(3));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tables;
    }
}
