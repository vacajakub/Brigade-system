package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.AddressDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.AdminDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CompanyDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AdminService {
    private final AdminDao adminDao;

    @Autowired
    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Transactional(readOnly = true)
    public List<Admin> findAll() {
        return adminDao.findAll();
    }


    @Transactional(readOnly = true)
    public Admin find(Integer id) {
        return adminDao.find(id);
    }

    @Transactional
    public void persist(Admin admin) {
        adminDao.persist(admin);
    }


    @Transactional
    public void update(Admin admin) {
        adminDao.update(admin);
    }


    @Transactional
    public void remove(Admin admin) {
        Objects.requireNonNull(admin);
        adminDao.remove(admin);
    }
}
