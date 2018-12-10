package cz.cvut.kbss.ear.brigade.service;


import cz.cvut.kbss.ear.brigade.dao.implementations.BrigadeDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.exception.BrigadeNotBelongToEmployerException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployerService {

    private final EmployerDao employerDao;
    private final BrigadeDao brigadeDao;
    private final WorkerDao workerDao;
    private final BrigadeService brigadeService;

    @Autowired
    public EmployerService(EmployerDao employerDao, BrigadeDao brigadeDao, WorkerDao workerDao,
                           BrigadeService brigadeService) {
        this.employerDao = employerDao;
        this.brigadeDao = brigadeDao;
        this.workerDao = workerDao;
        this.brigadeService = brigadeService;

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
    public void moveWorkerToBlacklist(Employer employer, int brigadeId, Worker worker) {
        try {
            final Brigade brigade = employer.findBrigadeById(brigadeId);

            brigade.getWorkers().remove(worker);
            brigade.getNoShowWorkers().add(worker);

            worker.getBrigades().remove(brigade);
            worker.getUnvisitedBrigades().add(brigade);

            brigadeDao.update(brigade);
            workerDao.update(worker);
        } catch (IllegalStateException e) {
            throw new BrigadeNotBelongToEmployerException("Brigade id" + brigadeId + " not belong to emolyer: " + employer.getEmail());
        }
    }

    @Transactional
    public void removeWorkerFromBrigade(Employer employer, int brigadeId, Worker worker) {
        try {
            Brigade brigade = employer.findBrigadeById(brigadeId);
            brigade.getWorkers().remove(worker);
            worker.getBrigades().remove(brigade);
            brigadeDao.update(brigade);
            workerDao.update(worker);
        } catch (IllegalStateException e) {
            throw new BrigadeNotBelongToEmployerException("Brigade id" + brigadeId + " not belong to employer: " + employer.getEmail());
        }
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

    public void sendEmailtoWorkersFromBrigade(Brigade brigade, String text) {
        //todo poslat email vsem prihlasenym brigadnikum nejaky email
    }

    @Transactional
    public int getCountOfPastBrigades(Employer employer) {
        return getPastBrigades(employer).size();
    }

    public void remove(Employer employer) {
        employer.setActive(false);
        getFutureBrigades(employer).forEach(brigadeService::removeBrigade);
        employerDao.update(employer);
    }


}
