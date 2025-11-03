package service;

import model.Employee;
import model.Position;

import java.util.*;

public class EmployeeService {
    private final Set<Employee> employees = new HashSet<>();

    public boolean addEmployee(Employee employee) {
        for (Employee e : employees) {
            if (e.getEmail().equalsIgnoreCase(employee.getEmail())) {
                System.out.println("Pracownik o emailu " + employee.getEmail() + " już istnieje");
                return false;
            }
        }
        employees.add(employee);
        System.out.println("Dodano: " + employee.getFirstName() + " " + employee.getLastName());
        return true;
    }

    public void printAllEmployees() {
        System.out.println("\nLista wszystkich:");
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    public List<Employee> findByCompany(String company) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getCompanyName().equalsIgnoreCase(company)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Employee> sortByLastName() {
        List<Employee> sortedList = new ArrayList<>(employees);
        Collections.sort(sortedList, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return e1.getLastName().compareToIgnoreCase(e2.getLastName());
            }
        });
        return sortedList;
    }

    public Map<Position, List<Employee>> groupByPosition() {
        Map<Position, List<Employee>> grouped = new HashMap<>();
        for (Employee e : employees) {
            Position pos = e.getPosition();
            if (!grouped.containsKey(pos)) {
                grouped.put(pos, new ArrayList<Employee>());
            }
            grouped.get(pos).add(e);
        }
        return grouped;
    }

    public Map<Position, Long> countByPosition() {
        Map<Position, Long> counts = new HashMap<>();
        for (Employee e : employees) {
            Position pos = e.getPosition();
            if (counts.containsKey(pos)) {
                counts.put(pos, counts.get(pos) + 1);
            } else {
                counts.put(pos, 1L);
            }
        }
        return counts;
    }

    public double getAverageSalary() {
        if (employees.isEmpty()) return 0.0;
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total / employees.size();
    }

    public Optional<Employee> getHighestPaidEmployee() {
        if (employees.isEmpty()) return Optional.empty();
        Employee maxEmp = null;
        double maxSalary = Double.MIN_VALUE;
        for (Employee e : employees) {
            if (maxEmp == null || e.getSalary() > maxSalary) {
                maxEmp = e;
                maxSalary = e.getSalary();
            }
        }
        return Optional.of(maxEmp);
    }

    public List<Employee> validateSalaryConsistency() {
        List<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            double base = e.getPosition().getBaseSalary();
            if (e.getSalary() < base) {
                result.add(e);
            }
        }
        return result;
    }

    public Map<String, model.CompanyStatistics> getCompanyStatistics() {
        Map<String, List<Employee>> grouped = new HashMap<>();

        for (Employee e : employees) {
            String company = e.getCompanyName();
            if (!grouped.containsKey(company)) {
                grouped.put(company, new ArrayList<Employee>());
            }
            grouped.get(company).add(e);
        }

        Map<String, model.CompanyStatistics> stats = new HashMap<>();
        for (String company : grouped.keySet()) {
            List<Employee> list = grouped.get(company);
            int count = list.size();
            double sum = 0.0;
            Employee highest = null;
            double maxSalary = Double.MIN_VALUE;

            for (Employee e : list) {
                sum += e.getSalary();
                if (highest == null || e.getSalary() > maxSalary) {
                    highest = e;
                    maxSalary = e.getSalary();
                }
            }

            double avg = count == 0 ? 0.0 : sum / count;
            String highestFullName = highest == null ? null : highest.getFirstName() + " " + highest.getLastName();

            model.CompanyStatistics cs = new model.CompanyStatistics(company, count, avg, highestFullName);
            stats.put(company, cs);
        }

        return stats;
    }

}
