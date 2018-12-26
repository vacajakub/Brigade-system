package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.exception.*;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


    @Test(expected = LateSignOnException.class)
    public void singOnToBrigadeThrowsLateSignOnException() {
        Employer employer = Generator.generateEmployer();
        Brigade brigade = Generator.generateBrigade(true);
        brigade.setEmployer(employer);
        brigade.getWorkers().add(worker);
        worker.addBrigade(brigade);
        em.persist(employer);
        em.persist(brigade);
        workerService.persist(worker);
        workerService.singOnToBrigade(worker, brigade);
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

    @Test
    public void addWorker() {
        Worker worker = Generator.generateWorker();
        worker.setUsername("worker1@seznam.cz");
        Worker worker2 = Generator.generateWorker();
        worker2.setUsername("worker2@seznam.cz");
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setMaxWorkers(2);

        em.persist(brigade);
        em.persist(worker);
        em.persist(worker2);

        workerService.singOnToBrigade(worker, brigade);
        workerService.singOnToBrigade(worker2, brigade);

        final Brigade brigadeResult = em.find(Brigade.class, brigade.getId());
        final Worker workerResult1 = em.find(Worker.class, worker.getId());
        final Worker workerResult2 = em.find(Worker.class, worker2.getId());

        assertEquals(2, brigadeResult.getWorkers().size());
        assertTrue(brigadeResult.getWorkers().stream().anyMatch(w -> w.getUsername().equals(worker.getUsername())));
        assertTrue(brigadeResult.getWorkers().stream().anyMatch(w -> w.getUsername().equals(worker2.getUsername())));
        assertEquals(1, workerResult1.getBrigades().size());
        assertEquals(1, workerResult2.getBrigades().size());

    }

    @Test(expected = BrigadeIsFullException.class)
    public void addWorkerThrowsBrigadeIsFullException() {
        Worker worker = Generator.generateWorker();
        worker.setUsername("worker1@seznam.cz");
        Worker worker2 = Generator.generateWorker();
        worker2.setUsername("worker2@seznam.cz");
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setMaxWorkers(1);

        em.persist(brigade);
        em.persist(worker);
        em.persist(worker2);

        workerService.singOnToBrigade(worker, brigade);
        workerService.singOnToBrigade(worker2, brigade);
    }


}