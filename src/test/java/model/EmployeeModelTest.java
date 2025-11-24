package model;

import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeModelTest {

    @Test
    void testEmployeeEqualityByEmail() {
        // Tworzymy dwóch pracowników z różnymi danymi,
        // ale tym samym adresem email.
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.MANAGER, 12000);
        Employee e2 = new Employee("Janusz", "Nowak", "jan@example.com", "OtherCorp", Position.PROGRAMISTA, 8000);

        // Sprawdzamy, że obiekty są uznawane za równe (equals)
        // oraz że ich hashCode jest taki sam – bo porównanie opiera się tylko na emailu.
        assertEquals(e1, e2, "Pracownicy z tym samym emailem powinni być równi");
        assertEquals(e1.hashCode(), e2.hashCode(), "HashCode powinien zależeć tylko od email");
    }

    @Test
    void testInvalidEmailThrowsException() {
        // Próba utworzenia pracownika z niepoprawnym adresem email
        // powinna rzucić wyjątek IllegalArgumentException.
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new Employee("Anna", "Nowak", "invalid-email", "TechCorp", Position.PROGRAMISTA, 8000)
        );

        // Sprawdzamy, że komunikat błędu zawiera informację o adresie email.
        assertTrue(ex.getMessage().contains("Adres email"));
    }

    @Test
    void testPositionBaseSalaryAndHierarchy() {
        // Sprawdzamy czy enum Position poprawnie zwraca bazowe pensje i poziomy hierarchii.
        assertEquals(25000, Position.PREZES.getBaseSalary());      // Prezes ma pensję bazową 25000
        assertEquals(1, Position.PREZES.getHierarchyLevel());      // Prezes ma najwyższy poziom hierarchii (1)

        assertEquals(3000, Position.STAZYSTA.getBaseSalary());     // Stażysta ma pensję bazową 3000
        assertEquals(5, Position.STAZYSTA.getHierarchyLevel());    // Stażysta ma najniższy poziom hierarchii (5)
    }
}
