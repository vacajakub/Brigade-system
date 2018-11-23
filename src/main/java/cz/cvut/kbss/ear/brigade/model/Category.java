package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Categories")
public class Category extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @OneToMany (mappedBy = "category")
    private List<Brigade> brigades;

    public Category() {
        this.brigades = new ArrayList<>();
    }

    public void addBrigade(Brigade brigade){
        this.brigades.add(brigade);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Brigade> getBrigades() {
        return brigades;
    }

    public void setBrigades(List<Brigade> brigades) {
        this.brigades = brigades;
    }
}
