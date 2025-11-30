package org.example.testing.doubles;

import org.example.Interfaces.EmployeeRepository;
import org.example.model.Employee;
import java.util.*;

public class EmployeeRepositoryFake implements EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }
}
