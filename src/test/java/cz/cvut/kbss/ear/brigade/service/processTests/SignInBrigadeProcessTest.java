package cz.cvut.kbss.ear.brigade.service.processTests;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.BaseServiceTestRunner;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.service.WorkerService;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static cz.cvut.kbss.ear.eshop.environment.Generator.generateWorker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class SignInBrigadeProcessTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private BrigadeService brigadeService;

    private Worker worker;


    @Before
    public void setUp() {
        worker = generateWorker();
    }

    @Test
    public void happyPath() {

        worker.setRole(Role.WORKER);
        // prihlaseni uzivatele
        SecurityUtils.setCurrentUser(new UserDetails(worker));

        worker.setUsername("worker1@seznam.cz");
        Brigade brigade = Generator.generateBrigade(false);
        brigade.setMaxWorkers(2);

        em.persist(brigade);
        em.persist(worker);

        // vyhledat vsechny dostupne brigady
        List<Brigade> brigadeList = brigadeService.findAll();

        assertEquals(1, brigadeList.size());
        Brigade brigade1 = brigadeList.get(0);


        // prihlaseni brigadnika na brigadu
        workerService.singOnToBrigade(worker, brigade1);

        final Worker workerResult1 = em.find(Worker.class, worker.getId());

        assertEquals(1, brigade1.getWorkers().size());
        assertTrue(brigade1.getWorkers().stream().anyMatch(w -> w.getUsername().equals(worker.getUsername())));
        assertEquals(1, workerResult1.getBrigades().size());

    }
}

