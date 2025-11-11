package service;

import org.example.model.ImportSummary;
import org.example.model.Position;
import org.example.service.EmployeeService;
import org.example.service.ImportService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImportServiceTest {

    private ImportService createImportService() {
        return new ImportService(new EmployeeService());
    }

    private Path createTempCsv(String content) throws IOException {
        Path tempFile = Files.createTempFile("employees", ".csv");
        Files.writeString(tempFile, content);
        return tempFile;
    }

    @Test
    void testEmptyFileReturnsError() throws IOException {
        Path file = createTempCsv("");
        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertEquals(0, summary.getImportedCount());
        assertFalse(summary.getErrors().isEmpty());
        assertTrue(summary.getErrors().get(0).contains("Plik jest pusty"));
    }

    @Test
    void testInvalidLineFormat() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski"; // za mało pól
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("nieprawidłowa liczba pól"));
    }

    @Test
    void testUnknownPosition() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,NIEZNANE,10000";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("Nieznane stanowisko"));
    }

    @Test
    void testInvalidSalary() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,abc";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("Nieprawidłowe wynagrodzenie"));
    }

    @Test
    void testSuccessfulImport() throws IOException {
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,12000\n" +
                "Anna,Nowak,anna@example.com,TechCorp,PROGRAMISTA,8000";
        Path file = createTempCsv(content);

        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(2, summary.getImportedCount());
        assertTrue(summary.getErrors().isEmpty());
    }
}
