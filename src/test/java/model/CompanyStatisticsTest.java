package model;

import org.example.model.CompanyStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyStatisticsTest {

    @Test
    void testCompanyStatisticsStoresDataCorrectly() {
        // Tworzymy obiekt statystyk firmy z przykładowymi danymi
        CompanyStatistics stats = new CompanyStatistics("TechCorp", 5, 10000.0, "Jan Kowalski");

        // Sprawdzamy czy dane zapisane w obiekcie są poprawnie zwracane przez gettery
        assertEquals("TechCorp", stats.getCompanyName());
        assertEquals(5, stats.getEmployeeCount());
        assertEquals(10000.0, stats.getAverageSalary());
        assertEquals("Jan Kowalski", stats.getHighestPaidFullName());
    }

    @Test
    void testToStringContainsExpectedInformation() {
        // Tworzymy obiekt statystyk firmy
        CompanyStatistics stats = new CompanyStatistics("TechCorp", 3, 12000.0, "Anna Nowak");

        // Wywołujemy metodę toString(), która powinna zwrócić opis obiektu w formie tekstu
        String output = stats.toString();

        // Sprawdzamy czy w tym opisie znajdują się kluczowe informacje
        assertTrue(output.contains("TechCorp"));          // nazwa firmy
        assertTrue(output.contains("Pracowników: 3"));    // liczba pracowników
        assertTrue(output.contains("Średnie wynagrodzenie")); // średnia pensja
        assertTrue(output.contains("Anna Nowak"));        // najwyżej opłacony pracownik
    }

    @Test
    void testHighestPaidFullNameCanBeNull() {
        // Tworzymy obiekt statystyk dla pustej firmy (brak pracowników)
        CompanyStatistics stats = new CompanyStatistics("EmptyCorp", 0, 0.0, null);

        // Sprawdzamy czy najwyżej opłacony pracownik może być null
        assertNull(stats.getHighestPaidFullName());

        // W metodzie toString() w takim przypadku powinien pojawić się znak "-"
        assertTrue(stats.toString().contains("-"));
    }
}
