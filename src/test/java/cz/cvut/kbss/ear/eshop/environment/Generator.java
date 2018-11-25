package cz.cvut.kbss.ear.eshop.environment;

import cz.cvut.kbss.ear.brigade.model.*;
import cz.cvut.kbss.ear.brigade.util.Constants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

    @PersistenceContext
    private static EntityManager em;

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static Worker generateWorker() {
        final Worker worker = new Worker();
        worker.setFirstName("FirstName" + randomInt());
        worker.setLastName("LastName" + randomInt());
        worker.setEmail("username" + randomInt() + "@kbss.felk.cvut.cz");
        worker.setPassword(Integer.toString(randomInt()));
        return worker;
    }

    public static Employer generateEmployer() {
        final Employer employer = new Employer();
        employer.setFirstName("FirstName" + randomInt());
        employer.setLastName("LastName" + randomInt());
        employer.setEmail("username" + randomInt() + "@brigade.cz");
        employer.setPassword(Integer.toString(randomInt()));
        return employer;
    }

    public static Company generateCompany() {
        final Address address = new Address();
        address.setCity("City" + randomInt());
        address.setStreet("Street" + randomInt());
        address.setZipCode("ZipCode" + randomInt());

        final Company company = new Company();
        company.setName("Name" + randomInt());
        company.setIco("ICO" + randomInt());
        company.setAddress(address);
        return company;
    }

    public static Brigade generateBrigade(boolean isPast) {
        final Brigade brigade = new Brigade();
        brigade.setSalaryPerHour(150 + randomInt());
        Date dateFrom;
        Date dateTo;
        long oneDay = 1000 * 60 * 60 * 24;
        if (isPast) {
            dateFrom = new Date(System.currentTimeMillis() - (oneDay * (2 + RAND.nextInt(5))));
            dateTo = new Date(System.currentTimeMillis() - (oneDay));

        } else {
            dateFrom = new Date(System.currentTimeMillis() + (oneDay));
            dateTo = new Date(System.currentTimeMillis() + (oneDay * (2 + RAND.nextInt(5))));

        }
        brigade.setDateFrom(dateFrom);
        brigade.setDateTo(dateTo);
        brigade.setTimeFrom(new Time(System.currentTimeMillis()));
        brigade.setTimeTo(new Time(System.currentTimeMillis() + (1000 * 60 * 60)));
        brigade.setDescription("Description" + randomInt());
        brigade.setDuration(2);
        brigade.setMaxWorkers(2 + RAND.nextInt(10));
        return brigade;
    }

}
