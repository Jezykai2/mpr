package model;
import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    public Employee(String firstName, String lastName, String email, String companyName, Position position, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
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
