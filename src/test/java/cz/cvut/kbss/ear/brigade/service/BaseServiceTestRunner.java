package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.config.PersistenceConfig;
import cz.cvut.kbss.ear.brigade.config.ServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class}) // Configuration classes for the tests
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "txManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BaseServiceTestRunner {
    @Test
    public void nothing(){}
}
