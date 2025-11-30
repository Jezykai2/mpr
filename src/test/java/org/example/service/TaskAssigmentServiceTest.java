package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.TaskAssignmentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskAssignmentServiceTest {

    @Test
    void testAssignTask_findsFirstAvailableWithSkills() {
        // Stub kalendarza – zwraca predefiniowaną listę dostępnych pracowników
        CalendarStub calendarStub = new CalendarStub(List.of(
                new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.PROGRAMISTA, 8000),
                new Employee("Anna", "Nowak", "anna@example.com", "SoftCorp", Position.MANAGER, 12000)
        ));

        // Fake repozytorium kompetencji – działa w pamięci
        CompetenceFake competenceFake = new CompetenceFake();
        competenceFake.addCompetence("jan@example.com", "Java");
        competenceFake.addCompetence("anna@example.com", "Excel");

        // Spy rejestracji przydziałów – zapisuje wszystkie przydzielenia
        ResourceAllocationSpy allocationSpy = new ResourceAllocationSpy();

        // Dummy konfiguracja – przekazywana do konstruktora, ale nieużywana
        ConfigDummy configDummy = new ConfigDummy();

        TaskAssignmentService service = new TaskAssignmentService(
                calendarStub, competenceFake, allocationSpy, configDummy
        );

        // Wywołanie logiki biznesowej
        boolean assigned = service.assignTask("TASK-123", List.of("Java"), 10);

        // Weryfikacja
        assertTrue(assigned);
        assertEquals(1, allocationSpy.getAssignments().size());
        assertEquals("jan@example.com", allocationSpy.getAssignments().get(0).getEmployeeEmail());
    }

    @Test
    void testAssignTask_noEmployeeWithRequiredSkill() {
        CalendarStub calendarStub = new CalendarStub(List.of(
                new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.PROGRAMISTA, 8000)
        ));
        CompetenceFake competenceFake = new CompetenceFake(); // brak kompetencji
        ResourceAllocationSpy allocationSpy = new ResourceAllocationSpy();
        ConfigDummy configDummy = new ConfigDummy();

        TaskAssignmentService service = new TaskAssignmentService(
                calendarStub, competenceFake, allocationSpy, configDummy
        );

        boolean assigned = service.assignTask("TASK-999", List.of("Python"), 5);

        assertFalse(assigned);
        assertTrue(allocationSpy.getAssignments().isEmpty());
    }
}
