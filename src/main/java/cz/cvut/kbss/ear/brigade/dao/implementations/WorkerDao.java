package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class WorkerDao extends BaseDao<Worker> {


    protected WorkerDao() {
        super(Worker.class);
    }

    public Worker findByEmail(String email) {
        try {
            return em.createNamedQuery("Worker.findByEmail", Worker.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
