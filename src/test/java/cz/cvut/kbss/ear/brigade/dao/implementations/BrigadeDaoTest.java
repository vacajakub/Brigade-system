package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.BaseDaoTestRunner;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;


public class BrigadeDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeDao brigadeDao;

    private Brigade brigade;
    private long currentTime = Calendar.getInstance().getTime().getTime();

    @Before
    public void setUp() throws Exception {
        brigade = new Brigade();
        brigade.setSalaryPerHour(100);
        brigade.setDateFrom(new Date(currentTime));
        brigade.setDateTo(new Date(currentTime + 60 * 60 * 1000));
    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findByDateFrom() throws Exception {
        em.persist(brigade);
        List<Brigade> b = brigadeDao.findByDateFrom(new Date(currentTime));
        Assert.assertEquals(brigade.getId(), b.get(0).getId());
    }

    @Test
    public void findByDateTo() throws Exception {
        em.persist(brigade);
        List<Brigade> b = brigadeDao.findByDateTo(new Date(currentTime + 60 * 60 * 1000));
        Assert.assertEquals(brigade.getId(), b.get(0).getId());
    }

    @Test
    public void findByCategory() throws Exception {
        Category category = new Category();
        category.setName("Cleaning");
        brigade.setCategory(category);
        category.getBrigades().add(brigade);
        em.persist(brigade);
        em.persist(category);
        List<Brigade> b = brigadeDao.findByCategory(category);
        Assert.assertEquals(brigade.getId(), b.get(0).getId());

    }

    @Test
    public void findByEmployer() throws Exception {
        Employer employer = Generator.generateEmployer();
        brigade.setEmployer(employer);
        em.persist(brigade);
        em.persist(employer);
        Brigade b = brigadeDao.findByEmployer(employer);
        Assert.assertEquals(brigade.getId(), b.getId());
    }

}