package org.example.testing.doubles;

import org.example.model.Employee;
import java.util.*;

public class EmployeeRepositoryFake {
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }
}
