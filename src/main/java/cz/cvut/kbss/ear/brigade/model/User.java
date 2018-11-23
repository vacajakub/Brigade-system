package cz.cvut.kbss.ear.brigade.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@MappedSuperclass
public abstract class User extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    @Pattern(regexp = "[A-Za-z ]*", message = "Must contain only letters and spaces.")
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    @Pattern(regexp = "[A-Za-z ]*", message = "Must contain only letters and spaces.")
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Must contain valid email address.")
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    public User() {
        this.role = Role.GUEST;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void erasePassword() {
        this.password = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    @Override
    public String toString() {
        return "User{" +
                firstName + " " + lastName +
                "(" + email + ")}";
    }
}
