package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.exception.DateToIsBeforeDateFromException;
import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class BrigadeServiceTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeService brigadeService;

    private Brigade brigade;

    @Before
    public void setUp() {
        brigade = Generator.generateBrigade(false);
    }

    @Test
    public void addBrigade() {
        Employer employer = Generator.generateEmployer();
        Address address = new Address();
        address.setCity("Praha");
        address.setStreet("Dlouha");
        Category category = new Category();
        category.setName("Test");


        em.persist(employer);
        em.persist(address);
        em.persist(category);


        brigadeService.create(employer, brigade, category, address);

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
        brigadeService.create(employer, brigade, category, address);
    }


    @Test
    public void removeBrigade() {
        Employer employer = Generator.generateEmployer();
        Category category = new Category();
        category.setName("Test");
        Worker worker = Generator.generateWorker();
        Worker worker2 = Generator.generateWorker();


        brigade.setEmployer(employer);
        employer.addBrigade(brigade);

        brigade.setCategory(category);
        category.addBrigade(brigade);

        List<Worker> workerList = new ArrayList<>();
        workerList.add(worker);
        workerList.add(worker2);
        brigade.setWorkers(workerList);
        worker.addBrigade(brigade);
        worker2.addBrigade(brigade);

        em.persist(employer);
        em.persist(category);
        em.persist(worker);
        em.persist(worker2);
        em.persist(brigade);


        brigadeService.removeBrigade(brigade);

        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());
        final Employer employerResult = em.find(Employer.class, employer.getId());
        final Category categoryResult = em.find(Category.class, category.getId());
        final Worker workerResult1 = em.find(Worker.class, worker.getId());
        final Worker workerResult2 = em.find(Worker.class, worker2.getId());

        assertNull("brigade has to be null", brigadeResult);
        assertTrue("Wrong employer result", employerResult.getBrigades().stream().noneMatch(b -> b.getId().equals(brigade.getId())));
        assertTrue("Wrong category result", categoryResult.getBrigades().stream().noneMatch(b -> b.getId().equals(brigade.getId())));
        assertTrue("Wrong worker 1 result", workerResult1.getBrigades().stream().noneMatch(b -> b.getId().equals(brigade.getId())));
        assertTrue("Wrong worker 2 result", workerResult2.getBrigades().stream().noneMatch(b -> b.getId().equals(brigade.getId())));
    }

}