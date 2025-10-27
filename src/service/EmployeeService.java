package service;
import model.Employee;
import model.Position;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {
    private final Set<Employee> employees = new HashSet<>();


    public boolean addEmployee(Employee employee) {
        if (employees.stream().anyMatch(e -> e.getEmail().equalsIgnoreCase(employee.getEmail()))) {
            System.out.println("Pracownik o emailu " + employee.getEmail() + " już istnieje");
            return false;
        }
        employees.add(employee);
        System.out.println("Dodano: " + employee.getFirstName() + " " + employee.getLastName());
        return true;
    }

    public void printAllEmployees() {
        System.out.println("\n Lista wszystkich:");
        employees.forEach(System.out::println);
    }

    public List<Employee> findByCompany(String company) {
        return employees.stream()
                .filter(e -> e.getCompanyName().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }

    public List<Employee> sortByLastName() {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::getLastName))
                .collect(Collectors.toList());
    }

    public Map<Position, List<Employee>> groupByPosition() {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getPosition));
    }

    public Map<Position, Long> countByPosition() {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
    }

    public double getAverageSalary() {
        return employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }

    public Optional<Employee> getHighestPaidEmployee() {
        return employees.stream()
                .max(Comparator.comparingDouble(Employee::getSalary));
    }
}
