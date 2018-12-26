package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.BaseDaoTestRunner;
import cz.cvut.kbss.ear.brigade.model.Employer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EmployerDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EmployerDao employerDao;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findByEmail() throws Exception {
        Employer employer = new Employer();
        employer.setFirstName("John");
        employer.setLastName("Doe");
        employer.setUsername("something@gmail.com");
        employer.setPassword("Asdasda");
        em.persist(employer);
        Employer e = employerDao.findByEmail("something@gmail.com");
        Assert.assertEquals(employer.getId(), e.getId());
    }

    @Test
    public void findByLastName() throws Exception {
    }

}