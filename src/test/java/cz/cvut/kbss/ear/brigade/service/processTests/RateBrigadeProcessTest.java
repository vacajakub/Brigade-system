package cz.cvut.kbss.ear.brigade.service.processTests;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
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

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class RateBrigadeProcessTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private WorkerService workerService;

    private Worker worker;

    private Employer employer;


    @Before
    public void setUp() {
        worker = Generator.generateWorker();
        employer = Generator.generateEmployer();
    }

    @Test
    public void happyPath() {

        worker.setRole(Role.WORKER);
        // prihlaseni uzivatele
        SecurityUtils.setCurrentUser(new UserDetails(worker));
        Brigade brigade = Generator.generateBrigade(true);
        worker.addBrigade(brigade);
        brigade.addWorker(worker);
        workerService.persist(worker);
        em.persist(brigade);

        List<Brigade> brigadeList = workerService.getPastBrigades(worker);

        assertEquals(1, brigadeList.size());
        Brigade brigade1 = brigadeList.get(0);
        // hodniceni brigady - pozitivni hodnoceni
        workerService.addThumbsUpToBrigade(worker, brigade1);

        final Worker workerResult = em.find(Worker.class, worker.getId());
        final Brigade brigadeResult = em.find(Brigade.class, brigade1.getId());

        assertEquals(1, workerResult.getBrigadesThumbsUps().size());
        assertEquals(1, brigadeResult.getWorkersThumbsUps().size());

    }
}
