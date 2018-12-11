/**
 * Copyright (C) 2016 Czech Technical University in Prague
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.ear.brigade.service.security;


import cz.cvut.kbss.ear.brigade.dao.implementations.EmployerDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.UserDao;
import cz.cvut.kbss.ear.brigade.dao.implementations.WorkerDao;
import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.User;
import cz.cvut.kbss.ear.brigade.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final WorkerDao workerDao;
    private final EmployerDao employerDao;

    @Autowired
    public UserDetailsService(WorkerDao workerDao, EmployerDao employerDao) {
        this.workerDao = workerDao;
        this.employerDao = employerDao;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Worker worker = workerDao.findByEmail(username);
        if (worker == null) {
            final Employer employer = employerDao.findByEmail(username);
            if (employer == null) {
                throw new UsernameNotFoundException("User with email " + username + " not found.");
            }
            return new cz.cvut.kbss.ear.brigade.security.model.UserDetails(employer);
        }
        return new cz.cvut.kbss.ear.brigade.security.model.UserDetails(worker);
    }
}
