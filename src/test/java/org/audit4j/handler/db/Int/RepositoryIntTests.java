package org.audit4j.handler.db.Int;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.audit4j.core.dto.AuditEvent;
import org.audit4j.core.dto.EventBuilder;
import org.audit4j.core.exception.HandlerException;
import org.audit4j.handler.db.DatabaseAuditHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryIntTests extends DBIntTestBase{

    DatabaseAuditHandler handler;
    
    @Before
    public void setup() {
       
    }

    @Test
    public void testRepositoryTag() {
        handler = new DatabaseAuditHandler();
        handler.setSeparate(true);
        handler.init();
        
        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        AuditEvent event = builder.build();
        event.setRepository("test");
        
        handler.setAuditEvent(event);
        
        try {
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        List<String> tables = getTableList();
        assertTrue(tables.contains("TEST_AUDIT"));
        System.out.println(getTableRecordCount("TEST_AUDIT"));
        
        System.out.println("sd");
        System.out.println(tables.toString());
    }

    @Test
    public void testTestRepositoryPrefixAndSuffix() {
        
        handler = new DatabaseAuditHandler();
        handler.setSeparate(true);
        handler.setTable_prefix("testprefix");
        handler.setTable_suffix("testsuffix");
        handler.init();
        
        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        AuditEvent event = builder.build();
        event.setRepository("test");
        
        handler.setAuditEvent(event);
        
        try {
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        List<String> tables = getTableList();
        
        System.out.println(tables.toString());
        assertTrue(tables.contains("TESTPREFIX_TEST_TESTSUFFIX"));
        
        System.out.println(getTableRecordCount("TESTPREFIX_TEST_TESTSUFFIX"));
        
        System.out.println("sd");
        System.out.println(tables.toString());
    }

    
    @After
    public void teardown() {
        handler.stop();
    }
}
