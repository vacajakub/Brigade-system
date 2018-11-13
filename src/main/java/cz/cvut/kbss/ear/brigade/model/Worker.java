package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Workers")
public class Worker extends User {



    @ManyToMany (mappedBy = "workers")
    private List<Brigade> brigades;



}
