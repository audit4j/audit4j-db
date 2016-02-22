package org.audit4j.handler.db.Int;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ConnectionPoolIntTest.class, DBHandlerEmbededInitIntTest.class,
        DBHandlerEmbededHandleIntTest.class, RepositoryIntTests.class })
public class IntegrationTestSuite {

}
