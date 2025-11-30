package org.example.testing.doubles;

import org.example.model.Employee;
import java.util.List;

// Stub – zwraca predefiniowaną listę dostępnych pracowników
public class CalendarStub {
    private final List<Employee> availableEmployees;

    public CalendarStub(List<Employee> availableEmployees) {
        this.availableEmployees = availableEmployees;
    }

    public List<Employee> getAvailableEmployees() {
        return availableEmployees;
    }
}
