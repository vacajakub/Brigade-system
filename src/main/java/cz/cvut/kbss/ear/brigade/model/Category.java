package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EAR_CATEGORY")
public class Category extends AbstractEntity {

    private String name;

    @OneToMany (mappedBy = "category")
    private List<Brigade> brigades;

}
