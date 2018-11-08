package cz.cvut.kbss.ear.brigade.dao.interfaces;

import cz.cvut.kbss.ear.brigade.dao.implementations.UserDao;
import cz.cvut.kbss.ear.brigade.model.User;

public interface IUserDao extends GenericDao<User> {


    User findByUsername(String username);
}
