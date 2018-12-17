package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.Worker;
import cz.cvut.kbss.ear.brigade.model.Worker_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class WorkerDao extends BaseDao<Worker> {


    protected WorkerDao() {
        super(Worker.class);
    }


    public List<Worker> findAll() {
        try {
            return em.createNamedQuery("Worker.findAll", Worker.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Worker findByEmail(String email) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Worker> cq = cb.createQuery(Worker.class);
        final Root<Worker> workerRoot = cq.from(Worker.class);
        cq.where(cb.equal(workerRoot.get(Worker_.email), email));
        try {
            return em.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Worker> findByLastName(String lastName) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Worker> cq = cb.createQuery(Worker.class);
        final Root<Worker> workerRoot = cq.from(Worker.class);
        cq.where(cb.equal(workerRoot.get(Worker_.lastName), lastName));
        try {
            return em.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
