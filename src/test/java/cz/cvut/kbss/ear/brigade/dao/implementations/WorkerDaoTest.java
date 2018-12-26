package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.BaseDaoTestRunner;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class WorkerDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkerDao workerDao;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findByEmail() throws Exception {
        String email = "our@gmail.com";
        Worker worker = Generator.generateWorker();
        worker.setUsername(email);
        em.persist(worker);
        Worker w = workerDao.findByEmail(email);
        Assert.assertEquals(worker.getId(), w.getId());
    }

    @Test
    public void findByLastName() throws Exception {
    }

}