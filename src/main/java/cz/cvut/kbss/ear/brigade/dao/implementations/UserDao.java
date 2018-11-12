package cz.cvut.kbss.ear.brigade.dao.implementations;

import cz.cvut.kbss.ear.brigade.dao.interfaces.IUserDao;
import cz.cvut.kbss.ear.brigade.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {

    public UserDao() {
        super(User.class);
    }

    public User findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
