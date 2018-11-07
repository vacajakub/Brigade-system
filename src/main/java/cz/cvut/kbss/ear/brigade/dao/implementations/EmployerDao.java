package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.stereotype.Repository;


@Repository
public class EmployerDao extends BaseDao<Employer> {

    protected EmployerDao() {
        super(Employer.class);
    }
}
