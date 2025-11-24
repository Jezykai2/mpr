package org.example.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeModelTest {

    @Test
    void testValidEmployeeCreation() {
        Employee e = new Employee("Jan", "Kowalski", "jan.kowalski@example.com",
                "TechCorp", Position.PROGRAMISTA, 5000);

        assertEquals("Jan", e.getFirstName());
        assertEquals("Kowalski", e.getLastName());
        assertEquals("jan.kowalski@example.com", e.getEmail()); // email zapisany lowercase
        assertEquals("TechCorp", e.getCompanyName());
        assertEquals(Position.PROGRAMISTA, e.getPosition());
        assertEquals(5000, e.getSalary());
    }

    @Test
    void testConstructorRejectsEmptyFirstName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Kowalski", "jan@example.com",
                        "TechCorp", Position.PROGRAMISTA, 4000));
    }

    @Test
    void testConstructorRejectsEmptyLastName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Jan", " ", "jan@example.com",
                        "TechCorp", Position.PROGRAMISTA, 4000));
    }

    @Test
    void testConstructorRejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Jan", "Kowalski", "janexamplecom",
                        "TechCorp", Position.PROGRAMISTA, 4000));
    }

    @Test
    void testConstructorRejectsEmptyCompanyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Jan", "Kowalski", "jan@example.com",
                        " ", Position.PROGRAMISTA, 4000));
    }

    @Test
    void testConstructorRejectsNullPosition() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Jan", "Kowalski", "jan@example.com",
                        "TechCorp", null, 4000));
    }

    @Test
    void testConstructorRejectsNegativeSalary() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Jan", "Kowalski", "jan@example.com",
                        "TechCorp", Position.PROGRAMISTA, -100));
    }

    @Test
    void testEqualsAndHashCodeBasedOnEmail() {
        Employee e1 = new Employee("Jan", "Kowalski", "jan@example.com",
                "TechCorp", Position.PROGRAMISTA, 5000);
        Employee e2 = new Employee("Jan", "Nowak", "jan@example.com",
                "OtherCorp", Position.MANAGER, 7000);

        assertEquals(e1, e2); // bo email taki sam
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void testToStringContainsKeyInformation() {
        Employee e = new Employee("Anna", "Nowak", "anna@example.com",
                "SoftCorp", Position.MANAGER, 8000);

        String output = e.toString();

        Assertions.assertAll(
                () -> assertTrue(output.contains("Anna Nowak")),
                () -> assertTrue(output.contains("SoftCorp")),
                () -> assertTrue(output.contains("anna@example.com")),
                () -> assertTrue(output.contains("wynagrodzenie: 8000"))
        );
    }

    @Test
    void testSettersUpdateValuesCorrectly() {
        Employee e = new Employee("Jan", "Kowalski", "jan.kowalski@example.com",
                "TechCorp", Position.PROGRAMISTA, 5000);

        // używamy setterów
        e.setFirstName("Adam");
        e.setLastName("Nowak");
        e.setCompanyName("SoftCorp");
        e.setPosition(Position.MANAGER);
        e.setSalary(9000);

        Assertions.assertAll(
                () -> assertEquals("Adam", e.getFirstName()),
                () -> assertEquals("Nowak", e.getLastName()),
                () -> assertEquals("SoftCorp", e.getCompanyName()),
                () -> assertEquals(Position.MANAGER, e.getPosition()),
                () -> assertEquals(9000, e.getSalary())
        );
    }
}
