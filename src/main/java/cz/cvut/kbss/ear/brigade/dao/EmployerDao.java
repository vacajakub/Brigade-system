package cz.cvut.kbss.ear.brigade.dao;


import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.stereotype.Repository;


@Repository
public class EmployerDao extends BaseDao<Employer> {

    protected EmployerDao(Class<Employer> type) {
        super(type);
    }
}
