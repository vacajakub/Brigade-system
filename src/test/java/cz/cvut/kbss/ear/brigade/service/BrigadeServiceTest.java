package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.exception.DateToIsBeforeDateFromException;
import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;

import static org.junit.Assert.*;


public class BrigadeServiceTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeService brigadeService;


    private Brigade brigade;

    @Before
    public void setUp() throws Exception {
        brigade = Generator.generateBrigade(false);
    }

    @Test
    public void addBrigade() throws Exception {
        Employer employer = Generator.generateEmployer();
        Address address = new Address();
        address.setCity("Praha");
        address.setStreet("Dlouha");
        Category category = new Category();
        category.setName("Test");


        em.persist(employer);
        em.persist(address);
        em.persist(category);
        brigadeService.persist(brigade);


        brigadeService.addBrigade(employer, brigade, category, address);

        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());
        final Employer employerResult = em.find(Employer.class, employer.getId());
        final Category categoryResult = em.find(Category.class, category.getId());

        Assert.assertEquals(brigadeResult.getId(), brigade.getId());
        Assert.assertEquals(brigadeResult.getWorkers().size(), 0);
        Assert.assertEquals(brigadeResult.getEmployer(), employerResult);
        Assert.assertEquals(brigadeResult.getCategory(), categoryResult);
        Assert.assertEquals(brigadeResult.getAddress().getCity(), "Praha");
    }

    @Test(expected = DateToIsBeforeDateFromException.class)
    public void addBrigadeThrowsDateToIsBeforeDateFromException() throws Exception {
        Employer employer = Generator.generateEmployer();
        Address address = new Address();
        address.setCity("Praha");
        address.setStreet("Dlouha");
        Category category = new Category();
        category.setName("Test");
        brigade.setDateFrom(new Date(System.currentTimeMillis() + 2 * Constants.ONE_DAY));
        brigade.setDateTo(new Date(System.currentTimeMillis()));
        brigadeService.addBrigade(employer, brigade, category, address);
    }

    @Test
    public void addWorker() throws Exception {
    }

    @Test
    public void removeWorkerFromBrigade() throws Exception {
    }

    @Test
    public void removeBrigade() throws Exception {
    }

    @Test
    public void findByFilters() throws Exception {
    }

}