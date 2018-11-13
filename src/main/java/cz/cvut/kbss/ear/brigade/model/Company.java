package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;

@Entity
@Table(name = "Companies")
public class Company extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String ico;


    @OneToOne
    private Address address;

}
