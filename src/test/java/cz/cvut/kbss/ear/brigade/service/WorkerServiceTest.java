package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.exception.LateSignOffException;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class WorkerServiceTest extends BaseServiceTestRunner {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkerService workerService;


    private Worker worker;

    @Before
    public void setUp() throws Exception {
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
    public void getFutureBrigades() throws Exception {
        worker.setBrigades(generateBrigades());
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
    public void singOffFromBrigade() throws Exception {
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
    public void singOffFromBrigadeThrowsLateSignOffException() throws Exception {
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
    public void getWorkerScore() throws Exception {
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

}