package model;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeModelTest {

    @Test
    void testEmployeeEqualityByEmail() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Janusz", "Nowak", "jan@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);

        assertEquals(e1, e2, "Pracownicy z tym samym emailem powinni być równi");
        assertEquals(e1.hashCode(), e2.hashCode(), "HashCode powinien zależeć tylko od email");
    }

    @Test
    void testInvalidEmailThrowsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "invalid-email", "TechCorp", Position.PROGRAMISTA, 8000)
        );
        assertTrue(ex.getMessage().contains("Adres email"));
    }

    @Test
    void testPositionBaseSalaryAndHierarchy() {
        assertEquals(25000, Position.PREZES.getBaseSalary());
        assertEquals(1, Position.PREZES.getHierarchyLevel());

        assertEquals(3000, Position.STAZYSTA.getBaseSalary());
        assertEquals(5, Position.STAZYSTA.getHierarchyLevel());
    }
}
