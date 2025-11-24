package org.example;

import org.example.model.Employee;
import org.example.model.ImportSummary;
import org.example.model.CompanyStatistics;
import org.example.service.EmployeeService;
import org.example.service.ImportService;
import org.example.service.ApiService;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        EmployeeService employeeService = new EmployeeService();
        ImportService importService = new ImportService(employeeService);
        ApiService apiService = new ApiService();

        try {
            System.out.println("Import z CSV");
            ImportSummary summary = importService.importFromCsv("resources/employees.csv");

            System.out.println("Zaimportowano: " + summary.getImportedCount());
            if (!summary.getErrors().isEmpty()) {
                System.out.println("\nBłędy importu:");
                for (String err : summary.getErrors()) {
                    System.out.println(" - " + err);
                }
            }

            System.out.println("\nPobieranie danych z API");

            List<Employee> apiEmployees = apiService.fetchEmployeesFromApi("https://jsonplaceholder.typicode.com/users");

            for (Employee e : apiEmployees) {
                employeeService.addEmployee(e);
            }
            System.out.println("Pobrano " + apiEmployees.size() + " pracowników z API.");

            System.out.println("\nWalidacja wynagrodzeń");
            List<Employee> lowSalary = employeeService.validateSalaryConsistency();
            if (lowSalary.isEmpty()) {
                System.out.println("Wynagrodzenia są zgodne.");
            } else {
                System.out.println("Pracownicy z wynagrodzeniem poniżej stawki:");
                for (Employee e : lowSalary) {
                    System.out.println(" - " + e.getFirstName() + " " + e.getLastName() + " (" + e.getPosition() + ")");
                }
            }

            System.out.println("\nStatystyki firm");
            Map<String, CompanyStatistics> stats = employeeService.getCompanyStatistics();
            for (Map.Entry<String, CompanyStatistics> entry : stats.entrySet()) {
                System.out.println("Firma: " + entry.getKey());
                System.out.println(entry.getValue());
                System.out.println("---------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}