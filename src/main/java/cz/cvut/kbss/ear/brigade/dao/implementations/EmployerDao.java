package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;


@Repository
public class EmployerDao extends BaseDao<Employer>  {

    protected EmployerDao() {
        super(Employer.class);
    }


    public List<Employer> findAll() {
        try {
            return em.createNamedQuery("Employer.findAll", Employer.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Employer findByEmail(String email) {
        try {
            return em.createNamedQuery("Employer.findByEmail", Employer.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Employer> findByLastName(String lastName) {
        try {
            return em.createNamedQuery("Employer.findByLastName", Employer.class).setParameter("lastName", lastName)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
