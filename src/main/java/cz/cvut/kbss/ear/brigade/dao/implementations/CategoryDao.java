package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.dao.interfaces.ICategoryDao;
import cz.cvut.kbss.ear.brigade.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends BaseDao<Category> implements ICategoryDao {

    protected CategoryDao() {
        super(Category.class);
    }
}
