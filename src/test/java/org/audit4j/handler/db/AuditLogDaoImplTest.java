package org.audit4j.handler.db;

import org.audit4j.core.dto.EventBuilder;
import org.audit4j.core.exception.HandlerException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class AuditLogDaoImplTest {
    static String ANY_TABLE_NAME = "any_table";

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

    @Test(expected = IllegalArgumentException.class)
    public void it_should_throw_error_on_invalid_table_name() throws HandlerException, SQLException {
        AuditLogDao logDao = new AuditLogDaoImpl("");
        Assert.fail("Should have thrown exception");
    }

    @Test
    public void testcreateAuditTableIFNotExist() throws HandlerException, SQLException {
        AuditLogDao logDao = new AuditLogDaoImpl(ANY_TABLE_NAME);
        Assert.assertNotNull(logDao);
    }

    @Test
    public void testwriteEvent() throws HandlerException, SQLException {
        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor)
                .addAction("myMethod")
                .addOrigin("Origin1")
                .addField("myParam1Name", "param1")
                .addField("myParam2Name", 2);

        AuditLogDao logDao = new AuditLogDaoImpl(ANY_TABLE_NAME);

        logDao.writeEvent(builder.build());
    }

    @After
    public void after() {
        server.shutdown();
        connectionFactory.stop();
    }
}
