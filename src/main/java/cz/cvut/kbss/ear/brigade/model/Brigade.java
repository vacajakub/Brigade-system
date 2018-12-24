package cz.cvut.kbss.ear.brigade.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cz.cvut.kbss.ear.brigade.exception.BrigadeIsFullException;

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

    public Brigade() {
        workers = new ArrayList<>();
        noShowWorkers = new ArrayList<>();
        workersThumbsUps = new ArrayList<>();
        workersThumbsDowns = new ArrayList<>();
        thumbsUp = thumbsDown = 0;
    }

    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private int salaryPerHour;

    @Basic(optional = false)
    @Column(nullable = false)
    private Date dateFrom;

    @Basic(optional = false)
    @Column(nullable = false)
    private Date dateTo;

    private String description;

    private String position;

    private Time timeFrom;


    private int duration;

    private int maxWorkers;

    private int thumbsUp;

    private int thumbsDown;

    @OneToOne
    private Address address;

    @ManyToOne
    @JsonIgnoreProperties("brigades")
    private Employer employer;

    @ManyToMany
    @JoinTable(name = "brigade_worker")
    @JsonIgnoreProperties("brigades")
    private List<Worker> workers;

    @ManyToMany
    @JoinTable(name = "brigade_worker_ThumbsUp")
    @JsonIgnoreProperties("brigadesThumbsUps")
    private List<Worker> workersThumbsUps;

    @ManyToMany
    @JoinTable(name = "brigade_worker_ThumbsDown")
    @JsonIgnoreProperties("brigadesThumbsDowns")
    private List<Worker> workersThumbsDowns;

    @ManyToMany
    @JoinTable(name = "blacklist")
    @JsonIgnoreProperties("unvisitedBrigades")
    private List<Worker> noShowWorkers;

    @ManyToOne
    @JsonIgnoreProperties("brigades")
    private Category category;

    public void addWorker(Worker worker) {
        if (workers.size() < maxWorkers)
            this.workers.add(worker);
        else
            throw new BrigadeIsFullException("Limit is " + maxWorkers + "!!!");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getMaxWorkers() {
        return maxWorkers;
    }

    public void setMaxWorkers(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public List<Worker> getNoShowWorkers() {
        return noShowWorkers;
    }

    public void setNoShowWorkers(List<Worker> noShowWorkers) {
        this.noShowWorkers = noShowWorkers;
    }


    public List<Worker> getWorkersThumbsUps() {
        return workersThumbsUps;
    }

    public void setWorkersThumbsUps(List<Worker> workersThumbsUps) {
        this.workersThumbsUps = workersThumbsUps;
    }

    public List<Worker> getWorkersThumbsDowns() {
        return workersThumbsDowns;
    }

    public void setWorkersThumbsDowns(List<Worker> workersThumbsDowns) {
        this.workersThumbsDowns = workersThumbsDowns;
    }

    @Override
    public String toString() {
        return "Brigade{" +
                "id=" + getId() + '\'' +
                "name='" + name + '\'' +
                ", salaryPerHour=" + salaryPerHour +
                '}';
    }
}
