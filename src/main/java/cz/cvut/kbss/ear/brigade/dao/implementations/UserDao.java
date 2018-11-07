package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDao extends BaseDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
