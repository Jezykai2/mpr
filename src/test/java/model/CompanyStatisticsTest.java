package model;

import org.example.model.CompanyStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyStatisticsTest {

    @Test
    void testCompanyStatisticsStoresDataCorrectly() {
        CompanyStatistics stats = new CompanyStatistics("TechCorp", 5, 10000.0, "Jan Kowalski");

        assertEquals("TechCorp", stats.getCompanyName());
        assertEquals(5, stats.getEmployeeCount());
        assertEquals(10000.0, stats.getAverageSalary());
        assertEquals("Jan Kowalski", stats.getHighestPaidFullName());
    }

    @Test
    void testToStringContainsExpectedInformation() {
        CompanyStatistics stats = new CompanyStatistics("TechCorp", 3, 12000.0, "Anna Nowak");

        String output = stats.toString();
        assertTrue(output.contains("TechCorp"));
        assertTrue(output.contains("Pracowników: 3"));
        assertTrue(output.contains("Średnie wynagrodzenie"));
        assertTrue(output.contains("Anna Nowak"));
    }

    @Test
    void testHighestPaidFullNameCanBeNull() {
        CompanyStatistics stats = new CompanyStatistics("EmptyCorp", 0, 0.0, null);

        assertNull(stats.getHighestPaidFullName());
        assertTrue(stats.toString().contains("-"));
    }
}
