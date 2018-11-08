package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.dao.interfaces.IEmployerDao;
import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.stereotype.Repository;


@Repository
public class EmployerDao extends BaseDao<Employer> implements IEmployerDao {

    protected EmployerDao() {
        super(Employer.class);
    }
}
