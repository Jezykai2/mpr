package service;

import model.Employee;
import model.ImportSummary;
import model.Position;
import exception.InvalidDataException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportService {

    private final EmployeeService employeeService;

    public ImportService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public ImportSummary importFromCsv(String path) {
        ImportSummary summary = new ImportSummary();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 0;

            line = br.readLine();
            lineNumber++;
            if (line == null) {
                summary.addError("Plik jest pusty: " + path);
                return summary;
            }

            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 6) {
                    summary.addError("Linia " + lineNumber + ": nieprawidłowa liczba pól. Treść: " + line);
                    continue;
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                String firstName = parts[0];
                String lastName = parts[1];
                String email = parts[2];
                String company = parts[3];
                String positionText = parts[4];
                String salaryText = parts[5];

                try {
                    Position position;
                    try {
                        position = Position.valueOf(positionText.toUpperCase());
                    } catch (IllegalArgumentException ex) {
                        throw new InvalidDataException("Nieznane stanowisko: " + positionText);
                    }

                    double salary;
                    try {
                        salary = Double.parseDouble(salaryText);
                    } catch (NumberFormatException ex) {
                        throw new InvalidDataException("Nieprawidłowe wynagrodzenie: " + salaryText);
                    }

                    if (salary <= 0) {
                        throw new InvalidDataException("Wynagrodzenie musi być dodatnie: " + salary);
                    }

                    Employee emp = new Employee(firstName, lastName, email, company, position, salary);
                    boolean added = employeeService.addEmployee(emp);
                    if (added) {
                        summary.incrementImported();
                    } else {
                        summary.addError("Linia " + lineNumber + ": pracownik z email " + email + " już istnieje.");
                    }

                } catch (InvalidDataException e) {
                    summary.addError("Linia " + lineNumber + ": " + e.getMessage());
                } catch (Exception e) {
                    summary.addError("Linia " + lineNumber + ": nieoczekiwany błąd: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            summary.addError("Błąd I/O przy czytaniu pliku: " + e.getMessage());
        }

        return summary;
    }
}
