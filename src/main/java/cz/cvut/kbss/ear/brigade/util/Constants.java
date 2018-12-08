package cz.cvut.kbss.ear.brigade.util;

import cz.cvut.kbss.ear.brigade.model.Role;

public final class Constants {

    /**
     * UTF-8 encoding identifier.
     */
    public static final String UTF_8_ENCODING = "UTF-8";

    public static final long LIMIT_FOR_SIGNING_OFF_OF_BRIGADE = 1000 * 60 * 60 * 24;

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    /**
     * Default user role.
     */
    public static final Role DEFAULT_ROLE = Role.WORKER;

    private Constants() {
        throw new AssertionError();
    }
}
