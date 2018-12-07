package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.AddressDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CompanyDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Company;
import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    private final CompanyDao companyDao;
    private final AddressDao addressDao;
    private final EmployerDao employerDao;

    @Autowired
    public CompanyService(CompanyDao companyDao, AddressDao addressDao, EmployerDao employerDao) {
        this.companyDao = companyDao;
        this.addressDao = addressDao;
        this.employerDao = employerDao;
    }

    // todo pouze admin
    @Transactional
    public void addEmployerToCompany(Employer employer, Company company) {
        employer.setCompany(company);
        employerDao.persist(employer);
    }

    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    @Transactional(readOnly = true)
    public Company find(Integer id) {
        return companyDao.find(id);
    }

    // todo pouze admin
    @Transactional
    public void persist(Company company) {
        companyDao.persist(company);
    }


    @Transactional
    public void update(Company company) {
        companyDao.update(company);
    }


    // todo vyresit co udelame z employerama + jejich brigadama -> taky SMAZAT ??? nebo null ???
    @Transactional
    public void remove(Company company) {
        Objects.requireNonNull(company);
        Address address = company.getAddress();
        companyDao.remove(company);
        addressDao.remove(address);
    }
}
