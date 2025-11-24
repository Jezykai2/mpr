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
        // Tworzymy pusty plik CSV
        Path file = createTempCsv("");

        // Importujemy dane – powinien pojawić się błąd "Plik jest pusty"
        ImportSummary summary = createImportService().importFromCsv(file.toString());

        assertEquals(0, summary.getImportedCount()); // nic nie zaimportowano
        assertFalse(summary.getErrors().isEmpty());  // lista błędów nie jest pusta
        assertTrue(summary.getErrors().get(0).contains("Plik jest pusty"));
    }

    @Test
    void testInvalidLineFormat() throws IOException {
        // Plik zawiera linię z za małą liczbą pól
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski"; // tylko 2 pola zamiast 6
        Path file = createTempCsv(content);

        // Import powinien zgłosić błąd formatu
        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("nieprawidłowa liczba pól"));
    }

    @Test
    void testUnknownPosition() throws IOException {
        // Plik zawiera nieznane stanowisko "NIEZNANE"
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,NIEZNANE,10000";
        Path file = createTempCsv(content);

        // Import powinien zgłosić błąd "Nieznane stanowisko"
        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("Nieznane stanowisko"));
    }

    @Test
    void testInvalidSalary() throws IOException {
        // Plik zawiera niepoprawną wartość wynagrodzenia ("abc" zamiast liczby)
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,abc";
        Path file = createTempCsv(content);

        // Import powinien zgłosić błąd "Nieprawidłowe wynagrodzenie"
        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(0, summary.getImportedCount());
        assertTrue(summary.getErrors().get(0).contains("Nieprawidłowe wynagrodzenie"));
    }

    @Test
    void testSuccessfulImport() throws IOException {
        // Plik zawiera dwóch poprawnych pracowników
        String content = "first,last,email,company,position,salary\n" +
                "Jan,Kowalski,jan@example.com,TechCorp,MANAGER,12000\n" +
                "Anna,Nowak,anna@example.com,TechCorp,PROGRAMISTA,8000";
        Path file = createTempCsv(content);

        // Import powinien zakończyć się sukcesem – 2 pracowników dodanych, brak błędów
        ImportSummary summary = createImportService().importFromCsv(file.toString());
        assertEquals(2, summary.getImportedCount());
        assertTrue(summary.getErrors().isEmpty());
    }
}
