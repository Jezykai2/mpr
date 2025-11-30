package org.example.testing.doubles;

import org.example.Interfaces.CalendarService;
import org.example.model.Employee;
import java.util.List;

public class CalendarStub implements CalendarService {
    private final List<Employee> employees;

    public CalendarStub(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public List<Employee> getAvailableEmployees() {
        return employees;
    }
}
