package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.BrigadeDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CategoryDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

    @Transactional
    public void addBrigade(Employer employer, Brigade brigade, Category category){
        brigade.setEmployer(employer);
        employer.addBrigade(brigade);
        brigade.setCategory(category);
        category.addBrigade(brigade);
        brigadeDao.update(brigade);
        employerDao.update(employer);
        categoryDao.update(category);
    }

    @Transactional
    public void addWorker(Brigade brigade, Worker workerToAdd){
        brigade.addWorker(workerToAdd);
        workerToAdd.addBrigade(brigade);
        brigadeDao.update(brigade);
        workerDao.update(workerToAdd);
    }

    @Transactional
    public void removeWorkerFromBrigade(Brigade brigade, Worker workerToRemove){
        Objects.requireNonNull(brigade);
        Objects.requireNonNull(workerToRemove);
        brigade.getWorkers().remove(workerToRemove);
        workerToRemove.getBrigades().remove(brigade);
        brigadeDao.update(brigade);
        workerDao.update(workerToRemove);
    }

    @Transactional
    public void removeBrigade(Brigade brigade){
        Category category = brigade.getCategory();
        Employer employer = brigade.getEmployer();
        brigade.getWorkers().forEach(worker ->{
                worker.getBrigades().remove(brigade);
                workerDao.update(worker);
        });
        category.getBrigades().remove(brigade);
        employer.getBrigades().remove(brigade);
        categoryDao.update(category);
        employerDao.update(employer);

    }
}