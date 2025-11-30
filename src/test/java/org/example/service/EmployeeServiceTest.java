package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.model.CompanyStatistics;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService service;
    private Employee e1, e2, e3;

    @BeforeEach
    void setUp() {
        service = new EmployeeService();
        e1 = new Employee("Jan", "Kowalski", "jan@example.com",
                "TechCorp", Position.PROGRAMISTA, 5000);
        e2 = new Employee("Anna", "Nowak", "anna@example.com",
                "SoftCorp", Position.MANAGER, 8000);
        e3 = new Employee("Piotr", "Zieliński", "piotr@example.com",
                "TechCorp", Position.PROGRAMISTA, 4000);

        service.addEmployee(e1);
        service.addEmployee(e2);
        service.addEmployee(e3);
    }

    @Test
    void testAddEmployee_duplicateEmailReturnsFalse() {
        Employee duplicate = new Employee("Jan", "Nowak", "jan@example.com",
                "OtherCorp", Position.MANAGER, 7000);

        boolean result = service.addEmployee(duplicate);
        assertFalse(result);
    }

    @Test
    void testFindByCompany_returnsCorrectEmployees() {
        List<Employee> techEmployees = service.findByCompany("TechCorp");

        assertAll(
                () -> assertEquals(2, techEmployees.size(), "Powinno być dwóch pracowników z TechCorp"),
                () -> assertTrue(techEmployees.contains(e1), "Lista powinna zawierać Jana Kowalskiego"),
                () -> assertTrue(techEmployees.contains(e3), "Lista powinna zawierać Piotra Zielińskiego")
        );
    }

    @Test
    void testSortByLastName_returnsSortedList() {
        List<Employee> sorted = service.sortByLastName();

        assertAll(
                () -> assertEquals(Arrays.asList(e1, e2, e3), sorted,
                        "Lista powinna być posortowana: Kowalski, Nowak, Zieliński")
        );
    }

    @Test
    void testGroupByPosition_returnsCorrectGrouping() {
        Map<Position, List<Employee>> grouped = service.groupByPosition();

        assertAll(
                () -> assertEquals(2, grouped.get(Position.PROGRAMISTA).size(),
                        "Powinno być dwóch programistów"),
                () -> assertEquals(1, grouped.get(Position.MANAGER).size(),
                        "Powinien być jeden manager")
        );
    }

    @Test
    void testCountByPosition_returnsCorrectCounts() {
        Map<Position, Long> counts = service.countByPosition();

        assertAll(
                () -> assertEquals(2L, counts.get(Position.PROGRAMISTA),
                        "Powinno być dwóch programistów"),
                () -> assertEquals(1L, counts.get(Position.MANAGER),
                        "Powinien być jeden manager")
        );
    }

    @Test
    void testGetAverageSalary_returnsCorrectValue() {
        double avg = service.getAverageSalary();
        double expected = (5000 + 8000 + 4000) / 3.0;

        assertAll(
                () -> assertEquals(expected, avg, "Średnia pensja powinna być poprawnie obliczona")
        );
    }

    @Test
    void testGetHighestPaidEmployee_returnsAnnaNowak() {
        Optional<Employee> highest = service.getHighestPaidEmployee();

        assertAll(
                () -> assertTrue(highest.isPresent(), "Powinien istnieć najwyżej opłacany pracownik"),
                () -> assertEquals(e2, highest.get(), "Najwyżej opłacana powinna być Anna Nowak")
        );
    }

    @Test
    void testValidateSalaryConsistency_returnsEmployeesBelowBase() {
        List<Employee> inconsistent = service.validateSalaryConsistency();

        assertAll(
                () -> assertTrue(inconsistent.contains(e3),
                        "Lista powinna zawierać Piotra Zielińskiego jako pracownika poniżej bazowej pensji")
        );
    }

    @Test
    void testGetCompanyStatistics_returnsCorrectStats() {
        Map<String, CompanyStatistics> stats = service.getCompanyStatistics();

        CompanyStatistics techStats = stats.get("TechCorp");
        CompanyStatistics softStats = stats.get("SoftCorp");

        assertAll(
                () -> assertEquals(2, techStats.getEmployeeCount(),
                        "TechCorp powinien mieć dwóch pracowników"),
                () -> assertEquals("Jan Kowalski", techStats.getHighestPaidFullName(),
                        "Najlepiej opłacany w TechCorp powinien być Jan Kowalski"),
                () -> assertEquals(1, softStats.getEmployeeCount(),
                        "SoftCorp powinien mieć jednego pracownika"),
                () -> assertEquals("Anna Nowak", softStats.getHighestPaidFullName(),
                        "Najlepiej opłacana w SoftCorp powinna być Anna Nowak")
        );
    }
}
