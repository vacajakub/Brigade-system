package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Entity
@Table(name = "Employers")
@NamedQueries({
        @NamedQuery(name = "Employer.findAll", query = "SELECT e FROM Employer e"),
        @NamedQuery(name = "Employer.findByEmail", query = "SELECT e FROM Employer e WHERE e.email = :email"),
        @NamedQuery(name = "Employer.findByLastName", query = "SELECT e from Employer e WHERE e.lastName = :lastName")
})
public class Employer extends User {

    private boolean isActive;

    @ManyToOne
    private Company company;

    @OrderBy("dateFrom ASC")
    @OneToMany(mappedBy = "employer")
    private List<Brigade> brigades;

    public Brigade findBrigadeById(int id) {
        return brigades.stream().filter(brigade -> brigade.getId() == id).collect(toSingleton());
    }

    public Employer() {
        this.brigades = new ArrayList<>();
        setRole(Role.EMPLOYER);
        this.isActive = true;
    }


    public void addBrigade(Brigade brigade) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}