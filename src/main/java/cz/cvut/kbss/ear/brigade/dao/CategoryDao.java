package cz.cvut.kbss.ear.brigade.dao;


import cz.cvut.kbss.ear.brigade.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends BaseDao<Category> {

    protected CategoryDao() {
        super(Category.class);
    }
}
