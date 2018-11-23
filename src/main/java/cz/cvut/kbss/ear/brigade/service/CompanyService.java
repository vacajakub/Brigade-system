package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.AddressDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CompanyDao;
import cz.cvut.kbss.ear.brigade.model.Address;
import cz.cvut.kbss.ear.brigade.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    private final CompanyDao companyDao;
    private final AddressDao addressDao;

    @Autowired
    public CompanyService(CompanyDao companyDao, AddressDao addressDao) {
        this.companyDao = companyDao;
        this.addressDao = addressDao;
    }

    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyDao.findAll();
    }


    @Transactional(readOnly = true)
    public Company find(Integer id) {
        return companyDao.find(id);
    }

    @Transactional
    public void persist(Company company) {
        companyDao.persist(company);
    }

    @Transactional
    public void update(Company company) {
        companyDao.update(company);
    }


    @Transactional
    public void remove(Company company) {
        Objects.requireNonNull(company);
        Address address = company.getAddress();
        companyDao.remove(company);
        addressDao.remove(address);
    }
}
