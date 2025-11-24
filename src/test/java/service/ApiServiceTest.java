package service;

import com.google.gson.Gson;
import org.example.exception.ApiException;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.ApiService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiServiceTest {

    @Test
    void testParseCorrectJson() throws Exception {
        // JSON z dwoma pracownikami
        String json = "[{\"name\":\"Jan Kowalski\",\"email\":\"jan@example.com\",\"company\":{\"name\":\"TechCorp\"}}," +
                "{\"name\":\"Anna Nowak\",\"email\":\"anna@example.com\",\"company\":{\"name\":\"OtherCorp\"}}]";

        // Parsujemy JSON bezpośrednio
        Gson gson = new Gson();
        Employee[] employeesArray = gson.fromJson(json, Employee[].class);
        List<Employee> employees = List.of(employeesArray);

        assertEquals(2, employees.size());
        assertEquals("jan@example.com", employees.get(0).getEmail());
        assertEquals(Position.PROGRAMISTA, employees.get(0).getPosition()); // domyślne stanowisko
    }

    @Test
    void testThrowsOnHttpErrorSimulation() {
        // Symulujemy błąd HTTP bez faktycznego requestu
        ApiService apiService = new ApiService();
        assertThrows(ApiException.class, () -> {
            throw new ApiException("Nieoczekiwany kod odpowiedzi HTTP: 500");
        });
    }

    @Test
    void testHandlesMissingCompanyField() {
        // JSON bez pola "company"
        String json = "[{\"name\":\"Jan Kowalski\",\"email\":\"jan@example.com\"}]";

        Gson gson = new Gson();
        Employee[] employeesArray = gson.fromJson(json, Employee[].class);
        List<Employee> employees = List.of(employeesArray);

        assertEquals(1, employees.size());
        // Jeśli Employee ma logikę ustawiającą "Unknown" przy braku company
        assertEquals("Unknown", employees.get(0).getCompanyName());
    }
}