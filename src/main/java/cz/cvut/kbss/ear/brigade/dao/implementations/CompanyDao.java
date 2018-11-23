package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.Company;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDao extends BaseDao<Company> {
    protected CompanyDao() {
        super(Company.class);
    }

}
