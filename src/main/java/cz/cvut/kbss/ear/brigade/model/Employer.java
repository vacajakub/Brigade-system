package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Employers")
@NamedQueries({
        @NamedQuery(name = "Employer.findAll", query = "SELECT e FROM Employer e"),
        @NamedQuery(name = "Employer.findByEmail", query = "SELECT e FROM Employer e WHERE e.email = :email"),
        @NamedQuery(name = "Employer.findByLastName", query = "SELECT e from Employer e WHERE e.lastName = :lastName")
})
public class Employer extends User {

    @ManyToOne
    private Company company;

    @OrderBy("dateFrom ASC")
    @OneToMany(mappedBy = "employer")
    private List<Brigade> brigades;

    public Employer() {
        this.brigades = new ArrayList<>();
    }


    public void addBrigade(Brigade brigade){
        this.brigades.add(brigade);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Brigade> getBrigades() {
        return brigades;
    }

    public void setBrigades(List<Brigade> brigades) {
        this.brigades = brigades;
    }
}

