package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.exception.BrigadeNotBelongToEmployerException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class EmployerServiceTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EmployerService employerService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Employer employer;


    @Before
    public void setUp() throws Exception {
        employer = Generator.generateEmployer();
    }

    @After
    public void tearDown() throws Exception {
    }

    private List<Brigade> generateBrigades() {
        List<Brigade> brigades = new ArrayList<>();
        boolean flag;
        for (int i = 0; i < 10; i++) {
            flag = i < 5;
            Brigade brigade = Generator.generateBrigade(flag);
            brigade.setThumbsUp(i);
            brigade.setThumbsDown(i + 2);
            if (i % 2 == 0)
                brigades.add(brigade);
            em.persist(brigade);
        }
        return brigades;
    }

    @Test
    public void getFutureBrigades() {
        //Address mockedAddress = mock(Address.class);
        // when(mockedCart.()).thenReturn(items);

        employer.setBrigades(generateBrigades());
        employerService.persist(employer);

        final Employer result = em.find(Employer.class, employer.getId());
        List<Brigade> futureBrigades = employer.getBrigades().stream()
                .filter(brigade -> brigade.getDateTo().getTime() >= new Date(System.currentTimeMillis()).getTime())
                .sorted(Comparator.comparing(Brigade::getId))
                .collect(Collectors.toList());

        List<Brigade> results = employerService.getFutureBrigades(result);
        results.sort(Comparator.comparing(Brigade::getId));

        assertEquals("Wrong size of list", futureBrigades.size(), results.size());

        for (int i = 0; i < futureBrigades.size(); i++) {
            assertEquals("Wrong id", futureBrigades.get(i).getId(), results.get(i).getId());
        }


    }

    @Test
    public void getPastBrigades() {
        employer.setBrigades(generateBrigades());
        employerService.persist(employer);

        final Employer result = em.find(Employer.class, employer.getId());
        List<Brigade> pastBrigades = employer.getBrigades().stream()
                .filter(brigade -> brigade.getDateTo().getTime() < new Date(System.currentTimeMillis()).getTime())
                .sorted(Comparator.comparing(Brigade::getId))
                .collect(Collectors.toList());

        List<Brigade> results = employerService.getPastBrigades(result);
        results.sort(Comparator.comparing(Brigade::getId));

        assertEquals("Wrong size of list", pastBrigades.size(), results.size());

        for (int i = 0; i < pastBrigades.size(); i++) {
            assertEquals("Wrong id", pastBrigades.get(i).getId(), results.get(i).getId());
        }
    }

    @Test
    public void moveWorkerToBlacklist() {
        Worker worker = Generator.generateWorker();
        Worker worker2 = Generator.generateWorker();
        Brigade brigade = Generator.generateBrigade(true);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        brigade.addWorker(worker2);
        worker2.addBrigade(brigade);
        List<Brigade> brigades = generateBrigades();
        brigades.add(brigade);
        employer.setBrigades(brigades);
        em.persist(brigade);
        em.persist(worker);
        em.persist(worker2);
        em.persist(employer);

        final Brigade resultBrigadeBefore = em.find(Brigade.class, brigade.getId());
        resultBrigadeBefore.getWorkers().size();
        resultBrigadeBefore.getNoShowWorkers().size();
        assertEquals("Wrong number of sign up workers", 2, resultBrigadeBefore.getWorkers().size());
        final Employer resultEmployerBefore = em.find(Employer.class, employer.getId());

        final Worker workerRes = em.find(Worker.class, worker.getId());

        assertEquals("Wrong number of worker in blacklist", 0, workerRes.getUnvisitedBrigades().size());

        employerService.moveWorkerToBlacklist(resultEmployerBefore, brigade.getId(), worker);

        final Employer resultEmployerAfter = em.find(Employer.class, resultEmployerBefore.getId());
        final Brigade brigadeFinal = resultEmployerAfter.findBrigadeById(brigade.getId());
        assertEquals("Wrong number of sign up workers", 1, brigadeFinal.getWorkers().size());

        final Worker resultWorker = em.find(Worker.class, worker.getId());
        assertEquals("Wrong number of worker in blacklist", 1, resultWorker.getUnvisitedBrigades().size());
    }

    @Test
    public void moveWorkerToBlacklistThrowsBrigadeNotBelongToEmployerException() {
        thrown.expect(BrigadeNotBelongToEmployerException.class);

        Employer employerSecond = Generator.generateEmployer();
        Worker worker = Generator.generateWorker();
        Worker worker2 = Generator.generateWorker();
        Brigade brigade = Generator.generateBrigade(true);
        brigade.setEmployer(employerSecond);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        brigade.addWorker(worker2);
        worker2.addBrigade(brigade);
        List<Brigade> brigades = generateBrigades();
        employer.setBrigades(brigades);
        em.persist(brigade);
        em.persist(worker);
        em.persist(worker2);
        em.persist(employer);

        employerService.moveWorkerToBlacklist(employer, brigade.getId(), worker);
    }

    @Test
    public void removeWorkerFromBrigade() {
        Worker worker = Generator.generateWorker();
        Brigade brigade = Generator.generateBrigade(true);
        brigade.setEmployer(employer);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        List<Brigade> brigades = generateBrigades();
        brigades.add(brigade);
        employer.setBrigades(brigades);
        em.persist(brigade);
        em.persist(worker);
        em.persist(employer);

        employerService.removeWorkerFromBrigade(employer, brigade.getId(), worker);

        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());
        final Worker workerResult = em.find(Worker.class, worker.getId());

        assertTrue(brigadeResult.getWorkers().isEmpty());
        assertTrue(workerResult.getBrigades().isEmpty());
        assertTrue(workerResult.getUnvisitedBrigades().isEmpty());
    }

    @Test
    public void removeWorkerFromBrigadeThrowsBrigadeNotBelongToEmployerException() {
        thrown.expect(BrigadeNotBelongToEmployerException.class);

        Worker worker = Generator.generateWorker();
        Brigade brigade = Generator.generateBrigade(true);
        brigade.setEmployer(employer);
        brigade.addWorker(worker);
        worker.addBrigade(brigade);
        List<Brigade> brigades = generateBrigades();
        employer.setBrigades(brigades);
        em.persist(brigade);
        em.persist(worker);
        em.persist(employer);

        employerService.removeWorkerFromBrigade(employer, brigade.getId(), worker);
    }

    @Test
    public void getEmployerScore() {
        employer.setBrigades(generateBrigades());
        em.persist(employer);

        final Employer result = em.find(Employer.class, employer.getId());
        Pair<Integer, Integer> score = employerService.getEmployerScore(result);

        assertEquals("ThumbsUp should be", 20, (int) score.getKey());
        assertEquals("ThumbsDown should be", 30, (int) score.getValue());
    }

    @Test
    public void getCountOfPastBrigades() {
        employer.setBrigades(generateBrigades());
        employerService.persist(employer);

        final Employer result = em.find(Employer.class, employer.getId());

        assertEquals("Wrong count of past brigades", 3, employerService.getCountOfPastBrigades(employer));
    }
}