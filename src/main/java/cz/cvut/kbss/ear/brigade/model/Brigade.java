package cz.cvut.kbss.ear.brigade.model;



import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "EAR_BRIGADE")
public class Brigade extends AbstractEntity {

    private int salaryPerHour;

    private Date dateFrom;

    private Date dateTo;

    private Time timeFrom;

    private Time timeTo;

    private int duration;

    @ManyToOne
    private Employer employer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "brigade_worker")
    private List<Worker> workers;

    @ManyToOne
    private Category category;


    public int getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(int salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }



    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }
}
