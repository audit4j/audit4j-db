package org.audit4j.handler.db.Int;

import org.audit4j.core.dto.EventBuilder;
import org.audit4j.core.exception.HandlerException;
import org.audit4j.handler.db.DatabaseAuditHandler;
import org.junit.Assert;
import org.junit.Test;

public class ConnectionPoolIntTest extends DBIntTestBase {

    @Test
    public void testConnectionPoolNormal() {
        DatabaseAuditHandler handler = new DatabaseAuditHandler();
        handler.setEmbedded("true");
        handler.setDb_connection_type("pooled");
        handler.setDb_datasourceClass("org.hsqldb.jdbc.JDBCDataSource");
        handler.init();

        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        handler.setAuditEvent(builder.build());
        try {
            handler.handle();
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertNotNull(getAuditTableRecordCount());
        System.out.println("stopping handler");
        handler.stop();
    }

    @Test
    public void testConnectionPoolConnections() {
        DatabaseAuditHandler handler = new DatabaseAuditHandler();
        handler.setEmbedded("true");
        handler.setDb_connection_type("pooled");
        handler.setDb_datasourceClass("org.hsqldb.jdbc.JDBCDataSource");
        handler.setDb_pool_maximumPoolSize(5);
        handler.init();

        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        handler.setAuditEvent(builder.build());
        try {
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertNotNull(getAuditTableRecordCount());
        System.out.println("stopping handler");
        handler.stop();
    }
    
    @Test
    public void testConnectionPoolParams() {
        DatabaseAuditHandler handler = new DatabaseAuditHandler();
        handler.setEmbedded("true");
        handler.setDb_connection_type("pooled");
        handler.setDb_datasourceClass("org.hsqldb.jdbc.JDBCDataSource");
        handler.setDb_pool_maximumPoolSize(5);
        handler.setDb_pool_autoCommit(false);
        handler.setDb_pool_connectionTimeout(10000L);
        handler.setDb_pool_idleTimeout(20000);
        handler.setDb_pool_maxLifetime(40000);
        handler.setDb_pool_minimumIdle(20000);
        handler.init();

        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        handler.setAuditEvent(builder.build());
        try {
            handler.handle();
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Assert.assertNotNull(getAuditTableRecordCount());
        System.out.println("stopping handler");
        handler.stop();
    }


    public void after() {
        
    }
}
