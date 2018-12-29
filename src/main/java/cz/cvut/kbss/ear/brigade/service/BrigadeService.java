package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.*;
import cz.cvut.kbss.ear.brigade.exception.DateToIsBeforeDateFromException;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.util.Constants;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrigadeService {

    private final BrigadeDao brigadeDao;
    private final EmployerDao employerDao;
    private final WorkerDao workerDao;
    private final CategoryDao categoryDao;
    private final AddressDao addressDao;

    @Autowired
    public BrigadeService(BrigadeDao brigadeDao, EmployerDao employerDao, WorkerDao workerDao, CategoryDao categoryDao,
                          AddressDao addressDao) {
        this.brigadeDao = brigadeDao;
        this.employerDao = employerDao;
        this.workerDao = workerDao;
        this.categoryDao = categoryDao;
        this.addressDao = addressDao;
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
    @PreAuthorize("hasRole('ADMIN') or principal.username == #brigade.employer.username")
    public void update(Brigade brigade) {
        brigadeDao.update(brigade);
    }

    @Transactional(readOnly = true)
    public Pair<Integer, Integer> getBrigadeScore(Brigade brigade) {
        int countThumbsUp = brigade.getWorkersThumbsUps().size();
        int countThumbsDown = brigade.getWorkersThumbsDowns().size();
        return new Pair<>(countThumbsUp, countThumbsDown);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    public void create(Employer employer, Brigade brigade, Category category, Address address) {
        if (brigade.getDateTo().getTime() < brigade.getDateFrom().getTime()) {
            throw new DateToIsBeforeDateFromException("DateTo must be after DateFrom!");
        }

        brigade.setEmployer(employer);
        employer.addBrigade(brigade);
        brigade.setCategory(category);
        category.addBrigade(brigade);
        brigade.setAddress(address);
        addressDao.persist(address);
        brigadeDao.persist(brigade);
        employerDao.update(employer);
        categoryDao.update(category);
    }



    @Transactional
    @PreAuthorize("hasRole('ADMIN') or principal.username == #brigade.employer.username")
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
        brigadeDao.remove(brigade);
    }


    @Transactional(readOnly = true)
    public List<Brigade> findByFilters(Category category, String city, Integer money, int days) {
        return brigadeDao.findAll()
                .stream()
                .filter(brigade -> brigade.getDateTo().getTime() <= System.currentTimeMillis() + days * Constants.ONE_DAY)
                .filter(brigade -> category == null || brigade.getCategory() == category)
                .filter(brigade -> city == null || brigade.getAddress().getCity().equals(city))
                .filter(brigade -> money == null || brigade.getSalaryPerHour() >= money)
                .sorted(Comparator.comparing(Brigade::getDateFrom))
                .collect(Collectors.toList());
    }
}
