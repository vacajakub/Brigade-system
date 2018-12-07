package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Workers")
@NamedQueries({
        @NamedQuery(name = "Worker.findAll", query = "SELECT w FROM Worker w"),
        @NamedQuery(name = "Worker.findByEmail", query = "SELECT w FROM Worker w WHERE w.email = :email"),
        @NamedQuery(name = "Worker.findByLastName", query = "SELECT w from Worker w WHERE w.lastName = :lastName")
})
public class Worker extends User {

    public Worker() {
        brigades = new ArrayList<>();
        unvisitedBrigades = new ArrayList<>();
        brigadesThumbsUps = new ArrayList<>();
        brigadesThumbsDowns = new ArrayList<>();
    }

    @OrderBy("dateFrom ASC")
    @ManyToMany (mappedBy = "workers")
    private List<Brigade> brigades;

    @ManyToMany (mappedBy = "workersThumbsUps")
    private List<Brigade> brigadesThumbsUps;

    @ManyToMany (mappedBy = "workersThumbsDowns")
    private List<Brigade> brigadesThumbsDowns;

    @ManyToMany (mappedBy = "noShowWorkers")
    private List<Brigade> unvisitedBrigades;

    public void addBrigade(Brigade brigade){
        this.brigades.add(brigade);
    }

    public List<Brigade> getBrigades() {
        return brigades;
    }

    public void setBrigades(List<Brigade> brigades) {
        this.brigades = brigades;
    }

    public List<Brigade> getUnvisitedBrigades() {
        return unvisitedBrigades;
    }

    public void setUnvisitedBrigades(List<Brigade> unvisitedBrigades) {
        this.unvisitedBrigades = unvisitedBrigades;
    }

    public List<Brigade> getBrigadesThumbsUps() {
        return brigadesThumbsUps;
    }

    public void setBrigadesThumbsUps(List<Brigade> brigadesThumbsUps) {
        this.brigadesThumbsUps = brigadesThumbsUps;
    }

    public List<Brigade> getBrigadesThumbsDowns() {
        return brigadesThumbsDowns;
    }

    public void setBrigadesThumbsDowns(List<Brigade> brigadesThumbsDowns) {
        this.brigadesThumbsDowns = brigadesThumbsDowns;
    }
}
