package cz.cvut.kbss.ear.brigade.model;

import org.springframework.security.crypto.password.PasswordEncoder;

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
    private String username;

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

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void erasePassword() {
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String email) {
        this.username = email;
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
                getId() +
                firstName + " " + lastName +
                "(" + username + ")}";
    }
}
