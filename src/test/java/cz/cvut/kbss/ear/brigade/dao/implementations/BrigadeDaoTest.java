package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;


public class BrigadeDaoTest extends BaseDaoTestRunner {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BrigadeDao brigadeDao;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void findAll() throws Exception {
    }

    @Test
    public void findByDateFrom() throws Exception {
    }

    @Test
    public void findByDateTo() throws Exception {
    }

    @Test
    public void findByDateCategory() throws Exception {
    }

    @Test
    public void findByEmployer() throws Exception {
    }

}