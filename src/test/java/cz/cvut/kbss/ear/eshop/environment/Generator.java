package cz.cvut.kbss.ear.eshop.environment;

import cz.cvut.kbss.ear.brigade.model.Employer;
import cz.cvut.kbss.ear.brigade.model.User;
import cz.cvut.kbss.ear.brigade.model.Worker;

import java.util.Random;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static Worker generateWorker() {
        final Worker user = new Worker();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setEmail("username" + randomInt() + "@kbss.felk.cvut.cz");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static Employer generateEmployer() {
        final Employer employer = new Employer();
        employer.setFirstName("FirstName" + randomInt());
        employer.setLastName("LastName" + randomInt());
        employer.setEmail("username" + randomInt() + "@brigade.cz");
        employer.setPassword(Integer.toString(randomInt()));
        return employer;
    }


}
