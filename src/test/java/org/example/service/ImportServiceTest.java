package org.example.service;

import org.example.model.ImportSummary;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImportServiceTest {

    // Pomocnicza metoda – tworzy nową instancję ImportService z pustym EmployeeService
    private ImportService createImportService() {
        return new ImportService(new EmployeeService());
    }

    // Pomocnicza metoda – tworzy tymczasowy plik CSV z podaną treścią
    private Path createTempCsv(String content) throws IOException {
        Path tempFile = Files.createTempFile("employees", ".csv");
        Files.writeString(tempFile, content);
        return tempFile;
    }

    @Test
    void testEmptyFileReturnsError() throws IOException {
        Path file = createTempCsv("");
        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertAll(
                () -> assertEquals(0, summary.getImportedCount(), "Nic nie powinno być zaimportowane"),
                () -> assertFalse(summary.getErrors().isEmpty(), "Lista błędów nie powinna być pusta"),
                () -> assertTrue(summary.getErrors().get(0).contains("Plik jest pusty"),
                        "Pierwszy błąd powinien zawierać komunikat o pustym pliku")
        );
    }

    @Test
    void testInvalidLineFormat() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski"; // tylko 2 pola zamiast 6
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertAll(
                () -> assertEquals(0, summary.getImportedCount(), "Nie powinno być importów"),
                () -> assertTrue(summary.getErrors().get(0).contains("nieprawidłowa liczba pól"),
                        "Powinien pojawić się błąd o liczbie pól")
        );
    }

    @Test
    void testUnknownPosition() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,NIEZNANE,10000";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertAll(
                () -> assertEquals(0, summary.getImportedCount(), "Nie powinno być importów"),
                () -> assertTrue(summary.getErrors().get(0).contains("Nieznane stanowisko"),
                        "Powinien pojawić się błąd o nieznanym stanowisku")
        );
    }

    @Test
    void testInvalidSalary() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,abc";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertAll(
                () -> assertEquals(0, summary.getImportedCount(), "Nie powinno być importów"),
                () -> assertTrue(summary.getErrors().get(0).contains("Nieprawidłowe wynagrodzenie"),
                        "Powinien pojawić się błąd o wynagrodzeniu")
        );
    }

    @Test
    void testSuccessfulImport() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,12000\n" +
                "Anna,Nowak,anna@example.com,TechCorp,PROGRAMISTA,8000";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertAll(
                () -> assertEquals(2, summary.getImportedCount(), "Powinno być zaimportowanych dwóch pracowników"),
                () -> assertTrue(summary.getErrors().isEmpty(), "Lista błędów powinna być pusta")
        );
    }
}
