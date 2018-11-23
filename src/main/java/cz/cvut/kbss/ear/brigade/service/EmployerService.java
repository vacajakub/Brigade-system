package cz.cvut.kbss.ear.brigade.service;


import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmployerService {

    private final EmployerDao employerDao;

    @Autowired
    public EmployerService(EmployerDao employerDao) {
        this.employerDao = employerDao;
    }

    @Transactional(readOnly = true)
    public Employer find(Integer id) {
        return employerDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Employer> findAll() {
        return employerDao.findAll();
    }

    @Transactional(readOnly = true)
    public void persist(Employer employer) {
        employerDao.persist(employer);
    }

    @Transactional(readOnly = true)
    public void update(Employer employer) {
        employerDao.update(employer);
    }


    @Transactional
    public List<Brigade> getFutureBrigades(Employer employer) {
        return WorkerService.filterBrigades(employer.getBrigades(), false);
    }

    @Transactional
    public List<Brigade> getPastBrigades(Employer employer) {
        return WorkerService.filterBrigades(employer.getBrigades(), true);
    }

    @Transactional
    public Pair<Integer, Integer> getEmployerScore(Employer employer) {
        int countPositive = 0;
        int countNegative = 0;
        for (Brigade brigade : employer.getBrigades()) {
            countPositive += brigade.getThumbsUp();
            countNegative += brigade.getThumbsDown();
        }
        return new Pair<>(countPositive, countNegative);
    }

    @Transactional
    public int getCountOfPastBrigades(Employer employer) {
        return getPastBrigades(employer).size();
    }


}
