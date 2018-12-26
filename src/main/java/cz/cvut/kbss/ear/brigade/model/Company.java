package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;

@Entity
@Table(name = "Companies")
public class Company extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private String ico;

    private String description;

    private boolean isActive;

    @OneToOne

    private Address address;

    public Company() {
        this.isActive = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
