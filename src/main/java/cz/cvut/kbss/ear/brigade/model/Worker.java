package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Workers")
@NamedQueries({
        @NamedQuery(name = "Worker.findByEmail", query = "SELECT u FROM Worker u WHERE u.email = :email")
})
public class Worker extends User {



    @ManyToMany (mappedBy = "workers")
    private List<Brigade> brigades;


    public List<Brigade> getBrigades() {
        return brigades;
    }

    public void setBrigades(List<Brigade> brigades) {
        this.brigades = brigades;
    }
}
