package cz.cvut.kbss.ear.brigade.model;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EAR_ADDRESS")
public class Address extends AbstractEntity {

    private String street;

    private String city;

    private String zipCode;


}
