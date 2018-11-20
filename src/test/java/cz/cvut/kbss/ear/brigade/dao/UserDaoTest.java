package cz.cvut.kbss.ear.brigade.dao;

import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

public class UserDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkerDao sut;

    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final Worker user = Generator.generateWorker();
        em.persist(user);

        final Worker result = sut.findByEmail(user.getEmail());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByEmail("unknownEmail"));
    }
}
