

package cz.cvut.kbss.ear.brigade.service.processTests;

import cz.cvut.kbss.ear.brigade.config.RestConfig;
import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.security.SecurityUtils;
import cz.cvut.kbss.ear.brigade.security.model.UserDetails;
import cz.cvut.kbss.ear.brigade.service.BaseServiceTestRunner;
import cz.cvut.kbss.ear.brigade.service.BrigadeService;
import cz.cvut.kbss.ear.brigade.util.Constants;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import cz.cvut.kbss.ear.eshop.environment.config.TestSecurityConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.Time;

import static cz.cvut.kbss.ear.eshop.environment.Generator.randomInt;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class})
public class CreateBrigadeProcessTest extends BaseServiceTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeService brigadeService;

    private Employer employer;
    private Brigade brigade;


    @Before
    public void setUp() {
        employer = Generator.generateEmployer();

        brigade = new Brigade();
        brigade.setSalaryPerHour(150);
        brigade.setName("Name");
        Date dateFrom = new Date(System.currentTimeMillis() + (Constants.ONE_DAY));
        Date dateTo = new Date(System.currentTimeMillis() + (Constants.ONE_DAY * 2));


        brigade.setDateFrom(dateFrom);
        brigade.setDateTo(dateTo);
        brigade.setTimeFrom(new Time(System.currentTimeMillis()));
        brigade.setDescription("Description" + randomInt());
        brigade.setDuration(2);
        brigade.setMaxWorkers(4);
    }

    @Test
    public void happyPath() {

        employer.setRole(Role.EMPLOYER);
        // prihlaseni uzivatele
        SecurityUtils.setCurrentUser(new UserDetails(employer));

        Address address = new Address();
        address.setCity("Praha");
        address.setStreet("Dlouha");
        Category category = new Category();
        category.setName("Test");


        em.persist(employer);
        em.persist(address);
        em.persist(category);
        // vytvoreni brigady
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
}

