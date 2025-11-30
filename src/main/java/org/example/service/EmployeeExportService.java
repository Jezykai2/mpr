package org.example.service;

import org.example.Interfaces.EmployeeRepository;
import org.example.Interfaces.Formatter;
import org.example.Interfaces.FileSystemService;
import org.example.model.Employee;

import java.util.List;

public class EmployeeExportService {
    private final EmployeeRepository repository;
    private final Formatter formatter;
    private final FileSystemService fileSystem;

    public EmployeeExportService(EmployeeRepository repository,
                                 Formatter formatter,
                                 FileSystemService fileSystem) {
        this.repository = repository;
        this.formatter = formatter;
        this.fileSystem = fileSystem;
    }

    public void exportEmployees(String fileName, String formatType) {
        List<Employee> employees = repository.getAllEmployees();
        String formatted = formatter.format(employees, formatType);

        fileSystem.writeFile(fileName, formatted);
    }
}
