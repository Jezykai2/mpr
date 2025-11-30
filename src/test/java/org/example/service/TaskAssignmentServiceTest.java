package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.Interfaces.CalendarService;
import org.example.Interfaces.CompetenceRepository;
import org.example.Interfaces.ResourceAllocationService;
import org.example.Interfaces.Logger;
import org.example.testing.doubles.CalendarStub;
import org.example.testing.doubles.CompetenceFake;
import org.example.testing.doubles.ResourceAllocationSpy;
import org.example.testing.doubles.ConfigDummy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskAssignmentServiceTest {

    @Test
    void testAssignTask_findsFirstAvailableWithSkills() {
        // Stub kalendarza – implementuje CalendarService
        CalendarService calendarStub = new CalendarStub(List.of(
                new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.PROGRAMISTA, 8000),
                new Employee("Anna", "Nowak", "anna@example.com", "SoftCorp", Position.MANAGER, 12000)
        ));

        // Fake repozytorium kompetencji – implementuje CompetenceRepository
        CompetenceRepository competenceFake = new CompetenceFake();
        ((CompetenceFake) competenceFake).addCompetence("jan@example.com", "Java");
        ((CompetenceFake) competenceFake).addCompetence("anna@example.com", "Excel");

        // Spy rejestracji przydziałów – implementuje ResourceAllocationService
        ResourceAllocationService allocationSpy = new ResourceAllocationSpy();

        // Dummy logger – implementuje Logger
        Logger configDummy = new ConfigDummy();

        TaskAssignmentService service = new TaskAssignmentService(
                calendarStub, competenceFake, allocationSpy, configDummy
        );

        // Wywołanie logiki biznesowej
        boolean assigned = service.assignTask("TASK-123", List.of("Java"), 10);

        // Weryfikacja
        assertAll(
                () -> assertTrue(assigned, "Pracownik powinien zostać przydzielony"),
                () -> assertEquals(1, ((ResourceAllocationSpy) allocationSpy).getAssignments().size(),
                        "Powinna być dokładnie jedna alokacja"),
                () -> assertEquals("jan@example.com",
                        ((ResourceAllocationSpy) allocationSpy).getAssignments().get(0).getEmployeeEmail(),
                        "Przydzielony powinien być Jan Kowalski")
        );
    }

    @Test
    void testAssignTask_noEmployeeWithRequiredSkill() {
        CalendarService calendarStub = new CalendarStub(List.of(
                new Employee("Jan", "Kowalski", "jan@example.com", "TechCorp", Position.PROGRAMISTA, 8000)
        ));
        CompetenceRepository competenceFake = new CompetenceFake(); // brak kompetencji
        ResourceAllocationService allocationSpy = new ResourceAllocationSpy();
        Logger configDummy = new ConfigDummy();

        TaskAssignmentService service = new TaskAssignmentService(
                calendarStub, competenceFake, allocationSpy, configDummy
        );

        boolean assigned = service.assignTask("TASK-999", List.of("Python"), 5);

        // Weryfikacja
        assertAll(
                () -> assertFalse(assigned, "Nie powinno być przydziału"),
                () -> assertTrue(((ResourceAllocationSpy) allocationSpy).getAssignments().isEmpty(),
                        "Lista przydziałów powinna być pusta")
        );
    }
}
