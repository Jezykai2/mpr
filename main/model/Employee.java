package main.model;

import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    public Employee(String firstName, String lastName, String email, String companyName, Position position, double salary) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Imię nie może być puste.");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwisko nie może być puste.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Adres email nie może być pusty.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Adres email jest niepoprawny: " + email);
        }

        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa firmy nie może być pusta.");
        }

        if (position == null) {
            throw new IllegalArgumentException("Stanowisko nie może być null.");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Wynagrodzenie nie może być ujemne.");
        }

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.companyName = companyName.trim();
        this.position = position;
        this.salary = salary;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee that = (Employee) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName +
                " | " + position +
                " | firma: " + companyName +
                " | email: " + email +
                " | wynagrodzenie: " + salary + " zł";
    }
}
