package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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
        try {
            return em.createNamedQuery("Worker.findByEmail", Worker.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Worker> findByLastName(String lastName) {
        try {
            return em.createNamedQuery("Worker.findByLastName", Worker.class).setParameter("lastName", lastName)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
