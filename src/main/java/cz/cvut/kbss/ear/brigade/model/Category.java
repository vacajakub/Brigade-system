package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Categories")
public class Category extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @OneToMany (mappedBy = "category")
    private List<Brigade> brigades;

}
