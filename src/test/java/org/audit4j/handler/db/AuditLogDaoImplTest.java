package org.audit4j.handler.db;

import java.sql.SQLException;

import org.audit4j.core.dto.EventBuilder;
import org.audit4j.core.exception.HandlerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuditLogDaoImplTest {

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
    public void testcreateAuditTableIFNotExist() {
        AuditLogDao logDao = AuditLogDaoImpl.getInstance();
      /*  try {
            logDao.createAuditTableIFNotExist();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    @Test
    public void testwriteEvent() {
        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));

        AuditLogDao logDao = AuditLogDaoImpl.getInstance();
        try {
            logDao.writeEvent(builder.build());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @After
    public void after() {
        server.shutdown();
        connectionFactory.stop();
    }
}
