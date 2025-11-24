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
        // Przed każdym testem tworzymy nową instancję serwisu,
        // żeby testy były niezależne od siebie.
        employeeService = new EmployeeService();
    }

    @Test
    void testAddUniqueEmployee() {
        // Dodajemy pracownika z unikalnym adresem email.
        Employee emp = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);

        // Oczekujemy, że metoda zwróci true (pracownik został dodany).
        assertTrue(employeeService.addEmployee(emp), "Dodanie unikalnego pracownika powinno zwrócić true");
    }

    @Test
    void testAddDuplicateEmployeeByEmail() {
        // Dodajemy dwóch pracowników z tym samym adresem email.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Janusz", "Nowak", "jan@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);

        // Oczekujemy, że dodanie drugiego pracownika się nie powiedzie (false).
        assertFalse(employeeService.addEmployee(e2), "Dodanie pracownika z istniejącym emailem powinno zwrócić false");
    }

    @Test
    void testFindByCompanyReturnsCorrectEmployees() {
        // Dodajemy dwóch pracowników do różnych firm.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Szukamy pracowników firmy "TechCorp".
        var result = employeeService.findByCompany("TechCorp");

        // Oczekujemy, że znajdzie się tylko jeden pracownik – Jan.
        assertEquals(1, result.size());
        assertEquals("jan@example.com", result.get(0).getEmail());
    }

    @Test
    void testSortByLastNameAlphabetically() {
        // Dodajemy dwóch pracowników z różnymi nazwiskami.
        Employee e1 = new Employee("Jan", "Zieliński", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Adamska", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Sortujemy po nazwisku.
        var sorted = employeeService.sortByLastName();

        // Oczekujemy, że pierwszy będzie Adamska, a drugi Zieliński.
        assertEquals("Adamska", sorted.get(0).getLastName());
        assertEquals("Zieliński", sorted.get(1).getLastName());
    }

    @Test
    void testGroupByPosition() {
        // Dodajemy pracowników na różnych stanowiskach.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Grupujemy pracowników wg stanowiska.
        var grouped = employeeService.groupByPosition();

        // Oczekujemy, że każda grupa zawiera po jednym pracowniku.
        assertTrue(grouped.containsKey(Position.MANAGER));
        assertTrue(grouped.containsKey(Position.PROGRAMISTA));
        assertEquals(1, grouped.get(Position.MANAGER).size());
        assertEquals(1, grouped.get(Position.PROGRAMISTA).size());
    }

    @Test
    void testCountByPosition() {
        // Dodajemy dwóch pracowników na tym samym stanowisku (MANAGER).
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.MANAGER, 15000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Liczymy ilu pracowników jest na stanowisku MANAGER.
        var counts = employeeService.countByPosition();
        assertEquals(2L, counts.get(Position.MANAGER));
    }

    @Test
    void testGetAverageSalary() {
        // Dodajemy dwóch pracowników z różnymi pensjami.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Średnia pensja powinna wynosić (12000 + 8000) / 2 = 10000.
        double avg = employeeService.getAverageSalary();
        assertEquals(10000.0, avg);
    }

    @Test
    void testGetHighestPaidEmployee() {
        // Dodajemy dwóch pracowników z różnymi pensjami.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Najlepiej opłacany powinien być Jan (12000).
        var highest = employeeService.getHighestPaidEmployee();
        assertTrue(highest.isPresent());
        assertEquals("jan@example.com", highest.get().getEmail());
    }

    @Test
    void testValidateSalaryConsistency() {
        // Dodajemy pracownika poniżej bazowej pensji (MANAGER ma bazową 12000).
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 10000);
        // Dodajemy pracownika z pensją równą bazowej (PROGRAMISTA ma bazową 8000).
        Employee e2 = new Employee("Anna", "Nowak", "anna@example.com", "TechCorp", Position.PROGRAMISTA, 8000);
        employeeService.addEmployee(e1);
        employeeService.addEmployee(e2);

        // Oczekujemy, że tylko Jan zostanie zwrócony jako pracownik z pensją poniżej bazowej.
        var inconsistent = employeeService.validateSalaryConsistency();
        assertEquals(1, inconsistent.size());
        assertEquals("jan@example.com", inconsistent.get(0).getEmail());
    }
}
