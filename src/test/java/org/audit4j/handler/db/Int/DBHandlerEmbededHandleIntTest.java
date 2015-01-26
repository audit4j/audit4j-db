package org.audit4j.handler.db.Int;

import org.audit4j.core.dto.EventBuilder;
import org.audit4j.core.exception.HandlerException;
import org.audit4j.handler.db.DatabaseAuditHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBHandlerEmbededHandleIntTest {

    DatabaseAuditHandler handler;

    @Before
    public void setup() {
        handler = new DatabaseAuditHandler();
        handler.init();
    }

    @Test
    public void testSendEvent() {
        String actor = "Dummy Actor";
        EventBuilder builder = new EventBuilder();
        builder.addActor(actor).addAction("myMethod").addOrigin("Origin1").addField("myParam1Name", "param1")
                .addField("myParam2Name", new Integer(2));
        handler.setAuditEvent(builder.build());
        try {
            handler.handle();
        } catch (HandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        handler.stop();
    }
}
