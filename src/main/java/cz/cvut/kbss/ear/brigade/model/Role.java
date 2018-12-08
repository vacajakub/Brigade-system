package cz.cvut.kbss.ear.brigade.model;

public enum Role {
    ADMIN("ROLE_ADMIN"), WORKER("ROLE_WORKER"), EMPLOYER("ROLE_EMPLOYER"), GUEST("ROLE_GUEST");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
