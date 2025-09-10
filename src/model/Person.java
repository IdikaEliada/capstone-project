package model;

import java.io.Serializable;

/**
 * Abstract Person class - base for Student (abstraction & encapsulation).
 */
public abstract class Person implements Serializable {
    private String id;
    private String firstName;
    private String lastName;

    public Person() { }

    public Person(String id, String firstName, String lastName) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
    }

    public String getId() { return id; }
    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("ID cannot be empty");
        this.id = id.trim();
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        if (firstName == null) firstName = "";
        this.firstName = firstName.trim();
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        if (lastName == null) lastName = "";
        this.lastName = lastName.trim();
    }

    public String getFullName() {
        return (getFirstName() + " " + getLastName()).trim();
    }

    @Override
    public String toString() {
        return getId() + " - " + getFullName();
    }
}