package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.exception.AlreadyRatedException;
import cz.cvut.kbss.ear.brigade.exception.BrigadeIsNotFinishedException;
import cz.cvut.kbss.ear.brigade.exception.LateSignOffException;
import cz.cvut.kbss.ear.brigade.exception.WorkerDidNotWorkOnBrigadeException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class WorkerServiceTest extends BaseServiceTestRunner {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkerService workerService;


    private Worker worker;

    @Before
    public void setUp() {
        worker = Generator.generateWorker();
    }

    private List<Brigade> generateBrigades() {
        List<Brigade> brigades = new ArrayList<>();
        boolean flag;
        for (int i = 0; i < 10; i++) {
            flag = i < 5;
            Brigade brigade = Generator.generateBrigade(flag);
            brigade.setThumbsUp(i);
            brigade.setThumbsDown(i + 2);
            if (i % 2 == 0) {
                brigades.add(brigade);
            }
            em.persist(brigade);
        }
        return brigades;
    }


    @Test
    public void getFutureBrigades() {
        worker.setBrigades(generateBrigades());
        worker.setRole(Role.ADMIN);
        SecurityUtils.setCurrentUser(new UserDetails(worker));
        workerService.persist(worker);
        List<Brigade> futureBrigades = worker.getBrigades()
                .stream()
                .filter(brigade -> brigade.getDateTo().getTime() >= new Date(System.currentTimeMillis()).getTime())
                .sorted(Comparator.comparing(Brigade::getId))
                .collect(Collectors.toList());
        final Worker result = em.find(Worker.class, worker.getId());
        List<Brigade> results = workerService.getFutureBrigades(result);
        results.sort(Comparator.comparing(Brigade::getId));
        assertEquals("Wrong size of list", futureBrigades.size(), results.size());

        for (int i = 0; i < futureBrigades.size(); i++) {
            assertEquals("Wrong id", futureBrigades.get(i).getId(), results.get(i).getId());
        }
    }

    @Test
    public void getFutureBrigadesWorker() {
        worker.setBrigades(generateBrigades());
        worker.setRole(Role.WORKER);
        SecurityUtils.setCurrentUser(new UserDetails(worker));
        workerService.persist(worker);
        List<Brigade> futureBrigades = worker.getBrigades()
                .stream()
                .filter(brigade -> brigade.getDateTo().getTime() >= new Date(System.currentTimeMillis()).getTime())
                .sorted(Comparator.comparing(Brigade::getId))
                .collect(Collectors.toList());
        final Worker result = em.find(Worker.class, worker.getId());
        List<Brigade> results = workerService.getFutureBrigades(result);
        results.sort(Comparator.comparing(Brigade::getId));
        assertEquals("Wrong size of list", futureBrigades.size(), results.size());

        for (int i = 0; i < futureBrigades.size(); i++) {
            assertEquals("Wrong id", futureBrigades.get(i).getId(), results.get(i).getId());
        }
    }

    @Test(expected = AccessDeniedException.class)
    public void getFutureBrigadesDifferentWorker() {
        worker.setBrigades(generateBrigades());
        worker.setRole(Role.WORKER);
        Worker differentWorker = Generator.generateWorker();
        differentWorker.setRole(Role.WORKER);
        SecurityUtils.setCurrentUser(new UserDetails(differentWorker));
        workerService.persist(worker);
        List<Brigade> futureBrigades = worker.getBrigades()
                .stream()
                .filter(brigade -> brigade.getDateTo().getTime() >= new Date(System.currentTimeMillis()).getTime())
                .sorted(Comparator.comparing(Brigade::getId))
                .collect(Collectors.toList());
        final Worker result = em.find(Worker.class, worker.getId());
        List<Brigade> results = workerService.getFutureBrigades(result);
        results.sort(Comparator.comparing(Brigade::getId));
        assertEquals("Wrong size of list", futureBrigades.size(), results.size());

        for (int i = 0; i < futureBrigades.size(); i++) {
            assertEquals("Wrong id", futureBrigades.get(i).getId(), results.get(i).getId());
        }
    }


    @Test
    public void singOffFromBrigade() {
        Employer employer = Generator.generateEmployer();
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setDateFrom(new Date(System.currentTimeMillis() + Constants.LIMIT_FOR_SIGNING_OFF_OF_BRIGADE * 2));
        brigade.setDateTo(new Date(System.currentTimeMillis() + Constants.LIMIT_FOR_SIGNING_OFF_OF_BRIGADE * 4));
        brigade.setEmployer(employer);
        brigade.getWorkers().add(worker);
        worker.addBrigade(brigade);
        em.persist(employer);
        em.persist(brigade);
        workerService.persist(worker);

        workerService.singOffFromBrigade(worker, brigade);


        final Worker workerResult = em.find(Worker.class, worker.getId());
        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());

        assertTrue(brigadeResult.getWorkers().isEmpty());
        assertTrue(workerResult.getBrigades().isEmpty());
    }


    @Test(expected = LateSignOffException.class)
    public void singOffFromBrigadeThrowsLateSignOffException() {
        Employer employer = Generator.generateEmployer();
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setDateFrom(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        brigade.setDateTo(new Date(System.currentTimeMillis() + Constants.LIMIT_FOR_SIGNING_OFF_OF_BRIGADE * 2));
        brigade.setEmployer(employer);
        brigade.getWorkers().add(worker);
        worker.addBrigade(brigade);
        em.persist(employer);
        em.persist(brigade);
        workerService.persist(worker);
        workerService.singOffFromBrigade(worker, brigade);
    }


    @Test
    public void getWorkerScore() {
        worker.addBrigade(Generator.generateBrigade(true));
        worker.addBrigade(Generator.generateBrigade(true));
        worker.addBrigade(Generator.generateBrigade(true));
        worker.addBrigade(Generator.generateBrigade(false));
        worker.getUnvisitedBrigades().add(Generator.generateBrigade(true));
        workerService.persist(worker);

        Pair<Integer, Integer> score = workerService.getWorkerScore(worker);
        assertEquals("Showed should be", 3, (int) score.getKey());
        assertEquals("NoShow should be", 1, (int) score.getValue());
    }

    @Test
    public void addThumbsUpToBrigade() {
        Brigade brigade = Generator.generateBrigade(true);
        worker.addBrigade(brigade);
        brigade.addWorker(worker);
        workerService.persist(worker);
        em.persist(brigade);

        workerService.addThumbsUpToBrigade(worker, brigade);

        final Worker workerResult = em.find(Worker.class, worker.getId());
        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());

        assertEquals(1, workerResult.getBrigadesThumbsUps().size());
        assertEquals(1, brigadeResult.getWorkersThumbsUps().size());
    }


    @Test(expected = BrigadeIsNotFinishedException.class)
    public void addThumbsUpToBrigadeThrowsBrigadeIsNotFinishedException() {
        Brigade brigade = Generator.generateBrigade(false);
        worker.addBrigade(brigade);
        brigade.addWorker(worker);
        workerService.persist(worker);
        em.persist(brigade);

        workerService.addThumbsUpToBrigade(worker, brigade);

        final Worker workerResult = em.find(Worker.class, worker.getId());
        assertEquals(0, workerResult.getBrigadesThumbsUps().size());
    }

    @Test(expected = AlreadyRatedException.class)
    public void addThumbsUpToBrigadeThrowsAlreadyRatedException() {
        Brigade brigade = Generator.generateBrigade(true);
        worker.addBrigade(brigade);
        worker.getBrigadesThumbsUps().add(brigade);
        brigade.getWorkersThumbsUps().add(worker);
        brigade.addWorker(worker);
        workerService.persist(worker);
        em.persist(brigade);

        workerService.addThumbsUpToBrigade(worker, brigade);

        final Worker workerResult = em.find(Worker.class, worker.getId());
        assertEquals(0, workerResult.getBrigadesThumbsUps().size());
    }

    @Test(expected = WorkerDidNotWorkOnBrigadeException.class)
    public void addThumbsUpToBrigadeThrowsWorkerDidNotWorkOnBrigadeException() {
        Brigade brigade = Generator.generateBrigade(true);
        workerService.persist(worker);
        em.persist(brigade);

        workerService.addThumbsUpToBrigade(worker, brigade);

        final Worker workerResult = em.find(Worker.class, worker.getId());
        assertEquals(0, workerResult.getBrigadesThumbsUps().size());
    }

}