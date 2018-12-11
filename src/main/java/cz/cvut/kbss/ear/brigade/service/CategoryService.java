package cz.cvut.kbss.ear.brigade.service;

import cz.cvut.kbss.ear.brigade.dao.implementations.BrigadeDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.CategoryDao;
import cz.cvut.kbss.ear.brigade.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;
    private final BrigadeDao brigadeDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao, BrigadeDao brigadeDao) {
        this.categoryDao = categoryDao;
        this.brigadeDao = brigadeDao;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryDao.findAll();
    }



    @Transactional(readOnly = true)
    public Category find(Integer id) {
        return categoryDao.find(id);
    }

    @Transactional
    public void persist(Category category) {
        categoryDao.persist(category);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void update(Category category) {
        categoryDao.update(category);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(Category category) {
        Objects.requireNonNull(category);
        category.getBrigades().forEach(brigade -> {
            brigade.setCategory(null);
            brigadeDao.update(brigade);

        });
        categoryDao.remove(category);
    }
}
