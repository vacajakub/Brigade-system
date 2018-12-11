package cz.cvut.kbss.ear.brigade.service;


import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
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

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class SecurityTest extends BaseServiceTestRunner {

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
}
