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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
