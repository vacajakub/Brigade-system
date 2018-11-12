package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EAR_COMPANY")
public class Company extends AbstractEntity {

    private String name;
    private String ico;


    @OneToOne
    private Address address;

}
