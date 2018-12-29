package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Admins")
public class Admin extends User{

    public Admin() {
        setRole(Role.ADMIN);
    }
}
