package org.audit4j.handler.db;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionFactoryTest {

    EmbededDBServer server;

    @Before
    public void before() {
        server = HSQLEmbededDBServer.getInstance();
        server.setUname("audit4jdbuser");
        server.setPassword("audit4jdbpassword");
        server.start();
    }

    @Test
    public void testInit() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connectionFactory.setDriver("org.hsqldb.jdbcDriver");
        connectionFactory.setUrl("jdbc:hsqldb:hsql://localhost/audit4j");
        connectionFactory.setUser("audit4jdbuser");
        connectionFactory.setPassword("audit4jdbpassword");
        connectionFactory.setConnectionType(ConnectionType.SINGLE);
        connectionFactory.init();
        connectionFactory.stop();
    }

    @Test
    public void testGetSingleConnection() {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        connectionFactory.setDriver("org.hsqldb.jdbcDriver");
        connectionFactory.setUrl("jdbc:hsqldb:hsql://localhost/audit4j");
        connectionFactory.setUser("audit4jdbuser");
        connectionFactory.setPassword("audit4jdbpassword");
        connectionFactory.setConnectionType(ConnectionType.SINGLE);
        connectionFactory.init();
        Connection conn = connectionFactory.getSingleConnection();
        assertNotNull(conn);
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        connectionFactory.stop();
        
    }
    
    @After
    public void after() {
        server.shutdown();
    }
}
