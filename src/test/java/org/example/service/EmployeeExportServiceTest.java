package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.Interfaces.EmployeeRepository;
import org.example.Interfaces.Formatter;
import org.example.Interfaces.FileSystemService;
import org.example.testing.doubles.EmployeeRepositoryFake;
import org.example.testing.doubles.FormatterStub;
import org.example.testing.doubles.FileSystemSpy;
import org.example.testing.doubles.FileSystemMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeExportServiceTest {

    @Test
    void testExportEmployeesToCsv_successfulExport() {
        // Fake repozytorium – implementuje EmployeeRepository
        EmployeeRepository repositoryFake = new EmployeeRepositoryFake();
        ((EmployeeRepositoryFake) repositoryFake).addEmployee(new Employee("Jan", "Kowalski", "jan@example.com",
                "TechCorp", Position.PROGRAMISTA, 8000));
        ((EmployeeRepositoryFake) repositoryFake).addEmployee(new Employee("Anna", "Nowak", "anna@example.com",
                "SoftCorp", Position.MANAGER, 12000));

        // Stub formatera – implementuje Formatter
        Formatter formatterStub = new FormatterStub("FAKE_CSV_DATA");

        // Spy systemu plików – implementuje FileSystemService
        FileSystemSpy fileSystemSpy = new FileSystemSpy();

        // Mock systemu plików – implementuje FileSystemService
        FileSystemMock fileSystemMock = new FileSystemMock("employees.csv", "FAKE_CSV_DATA");

        EmployeeExportService service = new EmployeeExportService(
                repositoryFake, formatterStub, fileSystemSpy
        );

        // Wywołanie logiki biznesowej
        service.exportEmployees("employees.csv", "CSV");

        // Weryfikacja
        assertAll(
                () -> assertEquals(1, fileSystemSpy.getWrites().size(),
                        "Spy powinien zarejestrować jeden zapis"),
                () -> assertEquals("employees.csv", fileSystemSpy.getWrites().get(0).getFileName(),
                        "Plik powinien mieć nazwę employees.csv"),
                () -> assertEquals("FAKE_CSV_DATA", fileSystemSpy.getWrites().get(0).getContent(),
                        "Zawartość powinna być FAKE_CSV_DATA"),
                () -> {
                    fileSystemMock.writeFile("employees.csv", "FAKE_CSV_DATA");
                    fileSystemMock.verify();
                }
        );
    }

    @Test
    void testExportEmployees_noEmployees() {
        EmployeeRepository repositoryFake = new EmployeeRepositoryFake(); // brak pracowników
        Formatter formatterStub = new FormatterStub("EMPTY_DATA");
        FileSystemSpy fileSystemSpy = new FileSystemSpy();
        FileSystemMock fileSystemMock = new FileSystemMock("employees.csv", "EMPTY_DATA");

        EmployeeExportService service = new EmployeeExportService(
                repositoryFake, formatterStub, fileSystemSpy
        );

        service.exportEmployees("employees.csv", "CSV");

        // Weryfikacja
        assertAll(
                () -> assertEquals(1, fileSystemSpy.getWrites().size(),
                        "Spy powinien zarejestrować jeden zapis"),
                () -> assertEquals("EMPTY_DATA", fileSystemSpy.getWrites().get(0).getContent(),
                        "Zawartość powinna być EMPTY_DATA"),
                () -> {
                    fileSystemMock.writeFile("employees.csv", "EMPTY_DATA");
                    fileSystemMock.verify();
                }
        );
    }
}
