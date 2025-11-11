package service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void testAddUniqueEmployee() {
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        assertTrue(employeeService.addEmployee(emp), "Dodanie unikalnego pracownika powinno zwrócić true");
    }

    @Test
    void testAddDuplicateEmployeeByEmail() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Janusz", "Nowak", "jan@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);

        assertFalse(employeeService.addEmployee(e2), "Dodanie pracownika z istniejącym emailem powinno zwrócić false");
    }

    @Test
    void testFindByCompanyReturnsCorrectEmployees() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var result = employeeService.findByCompany("TechCorp");
        assertEquals(1, result.size());
        assertEquals("jan@example.com", result.get(0).getEmail());
    }

    @Test
    void testSortByLastNameAlphabetically() {
        Employee e1 = new Employee("Jan", "Zieliński", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Adamska", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var sorted = employeeService.sortByLastName();
        assertEquals("Adamska", sorted.get(0).getLastName());
        assertEquals("Zieliński", sorted.get(1).getLastName());
    }

    @Test
    void testGroupByPosition() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var grouped = employeeService.groupByPosition();
        assertTrue(grouped.containsKey(Position.MANAGER));
        assertTrue(grouped.containsKey(Position.PROGRAMISTA));
        assertEquals(1, grouped.get(Position.MANAGER).size());
        assertEquals(1, grouped.get(Position.PROGRAMISTA).size());
    }

    @Test
    void testCountByPosition() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.MANAGER, 15000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var counts = employeeService.countByPosition();
        assertEquals(2L, counts.get(Position.MANAGER));
    }

    @Test
    void testGetAverageSalary() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        double avg = employeeService.getAverageSalary();
        assertEquals(10000.0, avg);
    }

    @Test
    void testGetHighestPaidEmployee() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var highest = employeeService.getHighestPaidEmployee();
        assertTrue(highest.isPresent());
        assertEquals("jan@example.com", highest.get().getEmail());
    }

    @Test
    void testValidateSalaryConsistency() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 10000); // poniżej bazowej 12000
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000); // równe bazowej
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        var inconsistent = employeeService.validateSalaryConsistency();
        assertEquals(1, inconsistent.size());
        assertEquals("jan@example.com", inconsistent.get(0).getEmail());
    }

}
