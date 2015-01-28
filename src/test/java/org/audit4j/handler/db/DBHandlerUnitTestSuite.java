package org.audit4j.handler.db;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AuditBaseDaoTest.class, AuditLogDaoImplTest.class, ConnectionFactoryTest.class })
public class DBHandlerUnitTestSuite {

}
