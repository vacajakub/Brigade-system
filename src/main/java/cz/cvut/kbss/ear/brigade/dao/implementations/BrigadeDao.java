package cz.cvut.kbss.ear.brigade.dao.implementations;


import cz.cvut.kbss.ear.brigade.model.Brigade;
import cz.cvut.kbss.ear.brigade.model.Category;
import cz.cvut.kbss.ear.brigade.model.Employer;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.List;

@Repository
public class BrigadeDao extends BaseDao<Brigade> {

    public BrigadeDao() {
        super(Brigade.class);
    }


    public List<Brigade> findAll() {
        try {
            return em.createNamedQuery("Brigade.findAll", Brigade.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<Brigade> findByDateFrom(Date dateFrom) {
        try {
            return em.createNamedQuery("Brigade.findByDateFrom", Brigade.class).setParameter("dateFrom", dateFrom)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Brigade> findByDateTo(Date dateTo) {
        try {
            return em.createNamedQuery("Brigade.findByDateTo", Brigade.class).setParameter("dateTo", dateTo)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Brigade> findByCategory(Category category) {
        try {
            return em.createNamedQuery("Brigade.findByCategory", Brigade.class).setParameter("category", category)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Brigade findByEmployer(Employer employer) {
        try {
            return em.createNamedQuery("Brigade.findByEmployer", Brigade.class).setParameter("employer", employer)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }




}
