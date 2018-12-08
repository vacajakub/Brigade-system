package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.model.Company;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.eshop.environment.Generator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

public class CompanyServiceTest extends BaseServiceTestRunner{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CompanyService companyService;

    private Company company;


    @Before
    public void setUp() {
        company = Generator.generateCompany();
    }

    @Test
    public void addEmployerToCompany() {
        Employer employer = Generator.generateEmployer();
        employer.setCompany(company);
        companyService.persist(company);
        em.persist(employer);

        Employer employerResult = em.find(Employer.class, employer.getId());

        assertEquals(company.getName(), employerResult.getCompany().getName());
    }

    @Test
    public void remove() {
        // todo - pote co se dohodneme co budeme vse mazat
    }
}