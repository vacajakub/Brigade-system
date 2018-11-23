package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.*;
import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BrigadeService {

    private final BrigadeDao brigadeDao;
    private final EmployerDao employerDao;
    private final WorkerDao workerDao;
    private final CategoryDao categoryDao;

    @Autowired
    public BrigadeService(BrigadeDao brigadeDao, EmployerDao employerDao, WorkerDao workerDao, CategoryDao categoryDao) {
        this.brigadeDao = brigadeDao;
        this.employerDao = employerDao;
        this.workerDao = workerDao;
        this.categoryDao = categoryDao;
    }

    @Transactional(readOnly = true)
    public Brigade find(Integer id) {
        return brigadeDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Brigade> findAll() {
        return brigadeDao.findAll();
    }

    @Transactional(readOnly = true)
    public void update(Brigade brigade) {
        brigadeDao.update(brigade);
    }

    @Transactional(readOnly = true)
    public void persist(Brigade brigade) {
        brigadeDao.persist(brigade);
    }

    @Transactional
    public void addBrigade(Employer employer, Brigade brigade, Category category, Address address) {
        brigade.setEmployer(employer);
        employer.addBrigade(brigade);
        brigade.setCategory(category);
        category.addBrigade(brigade);
        brigade.setAddress(address);
        brigadeDao.update(brigade);
        employerDao.update(employer);
        categoryDao.update(category);
    }

    @Transactional
    public void addWorker(Brigade brigade, Worker workerToAdd) {
        brigade.addWorker(workerToAdd);
        workerToAdd.addBrigade(brigade);
        brigadeDao.update(brigade);
        workerDao.update(workerToAdd);
    }

    @Transactional
    public void removeWorkerFromBrigade(Brigade brigade, Worker workerToRemove) {
        Objects.requireNonNull(brigade);
        Objects.requireNonNull(workerToRemove);
        brigade.getWorkers().remove(workerToRemove);
        workerToRemove.getBrigades().remove(brigade);
        brigadeDao.update(brigade);
        workerDao.update(workerToRemove);
    }

    @Transactional
    public void removeBrigade(Brigade brigade) {
        Category category = brigade.getCategory();
        Employer employer = brigade.getEmployer();
        brigade.getWorkers().forEach(worker -> {
            worker.getBrigades().remove(brigade);
            workerDao.update(worker);
        });
        category.getBrigades().remove(brigade);
        employer.getBrigades().remove(brigade);
        categoryDao.update(category);
        employerDao.update(employer);
    }


    @Transactional(readOnly = true)
    public List<Brigade> findByFilters(Category category, String city, Integer money, int days) {
        return brigadeDao.findAll()
                .stream()
                .filter(brigade -> brigade.getDateTo().getTime() <= System.currentTimeMillis() + days * Constants.LIMIT_FOR_SIGNING_OFF_OF_BRIGADE)
                .filter(brigade -> category == null || brigade.getCategory() == category)
                .filter(brigade -> city == null || brigade.getAddress().getCity().equals(city))
                .filter(brigade -> money == null || brigade.getSalaryPerHour() >= money)
                .sorted(Comparator.comparing(Brigade::getDateFrom))
                .collect(Collectors.toList());
    }
}
