package cz.cvut.kbss.ear.brigade.model;


import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Brigades")
@NamedQueries({
        @NamedQuery(name = "Brigade.findAll", query = "SELECT b from Brigade b"),
        @NamedQuery(name = "Brigade.findByDateFrom", query = "SELECT b from Brigade b where b.dateFrom = :dateFrom"),
        @NamedQuery(name = "Brigade.findByDateTo", query = "SELECT b from Brigade b where b.dateTo = :dateTo"),
        @NamedQuery(name = "Brigade.findByCategory", query = "SELECT b from Brigade b where b.category = :category"),
        @NamedQuery(name = "Brigade.findByEmployer", query = "SELECT b from Brigade b where  b.employer = :employer")
})
public class Brigade extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private int salaryPerHour;

    @Basic(optional = false)
    @Column(nullable = false)
    private Date dateFrom;

    @Basic(optional = false)
    @Column(nullable = false)
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

    public Brigade() {
        this.workers = new ArrayList<>();
    }

    public void addWorker(Worker worker){
        this.workers.add(worker);
    }


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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Time getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Time timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Time getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Time timeTo) {
        this.timeTo = timeTo;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
