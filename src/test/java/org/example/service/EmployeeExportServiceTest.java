package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.EmployeeExportService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeExportServiceTest {

    @Test
    void testExportEmployeesToCsv_successfulExport() {
        // Fake repozytorium – przechowuje pracowników w pamięci
        EmployeeRepositoryFake repositoryFake = new EmployeeRepositoryFake();
        repositoryFake.addEmployee(new Employee("Jan", "Kowalski", "jan@example.com",
                "TechCorp", Position.PROGRAMISTA, 8000));
        repositoryFake.addEmployee(new Employee("Anna", "Nowak", "anna@example.com",
                "SoftCorp", Position.MANAGER, 12000));

        // Stub formatera – zwraca predefiniowane stringi zamiast prawdziwego CSV
        FormatterStub formatterStub = new FormatterStub("FAKE_CSV_DATA");

        // Spy systemu plików – rejestruje operacje zapisu
        FileSystemSpy fileSystemSpy = new FileSystemSpy();

        // Mock systemu plików – weryfikuje poprawne parametry zapisu
        FileSystemMock fileSystemMock = new FileSystemMock("employees.csv", "FAKE_CSV_DATA");

        EmployeeExportService service = new EmployeeExportService(
                repositoryFake, formatterStub, fileSystemSpy, fileSystemMock
        );

        // Wywołanie logiki biznesowej
        service.exportEmployees("employees.csv", "CSV");

        // Weryfikacja – spy zarejestrował zapis
        assertEquals(1, fileSystemSpy.getWrites().size());
        assertEquals("employees.csv", fileSystemSpy.getWrites().get(0).getFileName());
        assertEquals("FAKE_CSV_DATA", fileSystemSpy.getWrites().get(0).getContent());

        // Mock sprawdza, że zapis odbył się z poprawnymi parametrami
        fileSystemMock.verify();
    }

    @Test
    void testExportEmployees_noEmployees() {
        EmployeeRepositoryFake repositoryFake = new EmployeeRepositoryFake(); // brak pracowników
        FormatterStub formatterStub = new FormatterStub("EMPTY_DATA");
        FileSystemSpy fileSystemSpy = new FileSystemSpy();
        FileSystemMock fileSystemMock = new FileSystemMock("employees.csv", "EMPTY_DATA");

        EmployeeExportService service = new EmployeeExportService(
                repositoryFake, formatterStub, fileSystemSpy, fileSystemMock
        );

        service.exportEmployees("employees.csv", "CSV");

        // Spy powinien zarejestrować zapis pustych danych
        assertEquals(1, fileSystemSpy.getWrites().size());
        assertEquals("EMPTY_DATA", fileSystemSpy.getWrites().get(0).getContent());

        // Mock weryfikuje, że zapis odbył się poprawnie
        fileSystemMock.verify();
    }
}
