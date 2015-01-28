package org.audit4j.handler.db;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuditBaseDaoTest {

    EmbededDBServer server;
    ConnectionFactory connectionFactory;

    @Before
    public void before() {
        server = HSQLEmbededDBServer.getInstance();
        server.setUname("audit4jdbuser");
        server.setPassword("audit4jdbpassword");
        server.start();

        connectionFactory = ConnectionFactory.getInstance();
        connectionFactory.setDriver("org.hsqldb.jdbcDriver");
        connectionFactory.setUrl("jdbc:hsqldb:hsql://localhost/audit4j");
        connectionFactory.setUser("audit4jdbuser");
        connectionFactory.setPassword("audit4jdbpassword");
        connectionFactory.setConnectionType(ConnectionType.SINGLE);
        connectionFactory.init();
    }

    @Test
    public void testgetConnection() {
        AuditBaseDao base = new AuditBaseDao();
        Connection conn = base.getConnection();
        assertNotNull(conn);
    }

    @After
    public void after() {
        server.shutdown();
        connectionFactory.stop();
    }

}
