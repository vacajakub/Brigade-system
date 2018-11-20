package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Employers")
public class Employer extends User {

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "employer")
    List<Brigade> brigades;


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

