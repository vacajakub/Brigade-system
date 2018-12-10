package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.AddressDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CompanyDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Company;
import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    private final CompanyDao companyDao;
    private final EmployerDao employerDao;
    private final EmployerService employerService;

    @Autowired
    public CompanyService(CompanyDao companyDao, EmployerDao employerDao,
                          EmployerService employerService) {
        this.companyDao = companyDao;
        this.employerDao = employerDao;
        this.employerService = employerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void addEmployerToCompany(Employer employer, Company company) {
        employer.setCompany(company);
        employerDao.persist(employer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    @Transactional(readOnly = true)
    public Company find(Integer id) {
        return companyDao.find(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void persist(Company company) {
        companyDao.persist(company);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void update(Company company) {
        companyDao.update(company);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void remove(Company company) {
        Objects.requireNonNull(company);
        company.setActive(false);
        companyDao.update(company);
        employerDao.findAll()
                .stream()
                .filter(employer -> employer.getCompany() == company)
                .forEach(employerService::remove);
    }
}
