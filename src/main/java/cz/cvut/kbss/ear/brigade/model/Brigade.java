package cz.cvut.kbss.ear.brigade.model;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

public class Brigade extends AbstractEntity {

    private int salaryPerHour;

    private DateTime fromDate;

    private DateTime toDate;

    private int duration;

    @ManyToOne
    private Employer employer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "brigade_worker")
    private List<Worker> workers;


    public int getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(int salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }

    public DateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
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
