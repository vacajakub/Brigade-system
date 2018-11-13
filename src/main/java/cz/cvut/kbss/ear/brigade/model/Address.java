package cz.cvut.kbss.ear.brigade.model;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Addresses")
public class Address extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String street;

    @Basic(optional = false)
    @Column(nullable = false)
    private String city;

    @Basic(optional = false)
    @Column(nullable = false)
    private String zipCode;


}
