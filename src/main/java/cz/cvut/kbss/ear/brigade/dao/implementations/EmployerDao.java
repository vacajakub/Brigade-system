package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.Employer_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Employer> cq = cb.createQuery(Employer.class);
        final Root<Employer> employerRoot = cq.from(Employer.class);
        cq.where(cb.equal(employerRoot.get(Employer_.email), email));
        try {
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Employer> findByLastName(String lastName) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Employer> cq = cb.createQuery(Employer.class);
        final Root<Employer> employerRoot = cq.from(Employer.class);
        cq.where(cb.equal(employerRoot.get(Employer_.lastName), lastName));
        try {
            return em.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
