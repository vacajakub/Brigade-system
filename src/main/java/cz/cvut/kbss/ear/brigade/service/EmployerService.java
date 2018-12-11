package cz.cvut.kbss.ear.brigade.service;


import cz.cvut.kbss.ear.brigade.dao.implementations.BrigadeDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.exception.BrigadeNotBelongToEmployerException;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Role;
import cz.cvut.kbss.ear.brigade.model.Worker;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployerService {

    private final EmployerDao employerDao;
    private final BrigadeDao brigadeDao;
    private final WorkerDao workerDao;
    private final BrigadeService brigadeService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployerService(EmployerDao employerDao, BrigadeDao brigadeDao, WorkerDao workerDao,
                           BrigadeService brigadeService, PasswordEncoder passwordEncoder) {
        this.employerDao = employerDao;
        this.brigadeDao = brigadeDao;
        this.workerDao = workerDao;
        this.brigadeService = brigadeService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    public Employer find(Integer id) {
        return employerDao.find(id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Employer> findAll() {
        return employerDao.findAll();
    }

    @Transactional(readOnly = true)
    public void persist(Employer employer) {
        employer.encodePassword(passwordEncoder);
        if (employer.getRole() == null) {
            employer.setRole(Role.EMPLOYER);
        }
        employerDao.persist(employer);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public void update(Employer employer) {
        employerDao.update(employer);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public List<Brigade> getFutureBrigades(Employer employer) {
        return WorkerService.filterBrigades(employer.getBrigades(), false);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public List<Brigade> getPastBrigades(Employer employer) {
        return WorkerService.filterBrigades(employer.getBrigades(), true);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public void moveWorkerToBlacklist(Employer employer, Brigade brigade, Worker worker) {
        try {
            employer.findBrigadeById(brigade.getId());

            brigade.getWorkers().remove(worker);
            brigade.getNoShowWorkers().add(worker);

            worker.getBrigades().remove(brigade);
            worker.getUnvisitedBrigades().add(brigade);

            brigadeDao.update(brigade);
            workerDao.update(worker);
        } catch (IllegalStateException e) {
            throw new BrigadeNotBelongToEmployerException("Brigade id" + brigade.getId() + " not belong to emolyer: " + employer.getEmail());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public void removeWorkerFromBrigade(Employer employer, Brigade brigade, Worker worker) {
        try {
            employer.findBrigadeById(brigade.getId());

            brigade.getWorkers().remove(worker);
            worker.getBrigades().remove(brigade);
            brigadeDao.update(brigade);
            workerDao.update(worker);
        } catch (IllegalStateException e) {
            throw new BrigadeNotBelongToEmployerException("Brigade id" + brigade.getId() + " not belong to employer: " + employer.getEmail());
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

    @PreAuthorize("hasRole('ADMIN') or principal.username == #employer.email")
    public void remove(Employer employer) {
        employer.setActive(false);
        getFutureBrigades(employer).forEach(brigadeService::removeBrigade);
        employerDao.update(employer);
    }


}
