package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.ManyToOne;

public class Employer extends User {

    @ManyToOne
    private Company company;


}

