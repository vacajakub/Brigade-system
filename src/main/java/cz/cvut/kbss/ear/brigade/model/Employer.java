package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "EAR_EMPLOYER")
public class Employer extends User {

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "employer")
    List<Brigade> brigades;

}

