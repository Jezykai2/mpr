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
        assertEquals(2, techEmployees.size());
        assertTrue(techEmployees.contains(e1));
        assertTrue(techEmployees.contains(e3));
    }

    @Test
    void testSortByLastName_returnsSortedList() {
        List<Employee> sorted = service.sortByLastName();
        assertEquals(Arrays.asList(e1, e2, e3),
                sorted); // Kowalski, Nowak, Zieliński
    }

    @Test
    void testGroupByPosition_returnsCorrectGrouping() {
        Map<Position, List<Employee>> grouped = service.groupByPosition();
        assertEquals(2, grouped.get(Position.PROGRAMISTA).size());
        assertEquals(1, grouped.get(Position.MANAGER).size());
    }

    @Test
    void testCountByPosition_returnsCorrectCounts() {
        Map<Position, Long> counts = service.countByPosition();
        assertEquals(2L, counts.get(Position.PROGRAMISTA));
        assertEquals(1L, counts.get(Position.MANAGER));
    }

    @Test
    void testGetAverageSalary_returnsCorrectValue() {
        double avg = service.getAverageSalary();
        double expected = (5000 + 8000 + 4000) / 3.0;
        assertEquals(expected, avg);
    }

    @Test
    void testGetHighestPaidEmployee_returnsAnnaNowak() {
        Optional<Employee> highest = service.getHighestPaidEmployee();
        assertTrue(highest.isPresent());
        assertEquals(e2, highest.get());
    }

    @Test
    void testValidateSalaryConsistency_returnsEmployeesBelowBase() {
        // e3 ma 4000, a base salary PROGRAMISTA np. 5000 → powinien być zwrócony
        List<Employee> inconsistent = service.validateSalaryConsistency();
        assertTrue(inconsistent.contains(e3));
    }

    @Test
    void testGetCompanyStatistics_returnsCorrectStats() {
        Map<String, CompanyStatistics> stats = service.getCompanyStatistics();

        CompanyStatistics techStats = stats.get("TechCorp");
        assertEquals(2, techStats.getEmployeeCount());
        assertEquals("Jan Kowalski", techStats.getHighestPaidFullName());

        CompanyStatistics softStats = stats.get("SoftCorp");
        assertEquals(1, softStats.getEmployeeCount());
        assertEquals("Anna Nowak", softStats.getHighestPaidFullName());
    }
}
