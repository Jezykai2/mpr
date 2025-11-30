package org.example.service;

import com.google.gson.JsonObject;
import org.example.exception.ApiException;
import org.example.model.Employee;
import org.example.model.Position;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiServiceTest {

    private final ApiService apiService = new ApiService();

    @Test
    void testSafeGetAsString_returnsValue() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "Jan");

        Method method = ApiService.class.getDeclaredMethod("safeGetAsString", JsonObject.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(apiService, obj, "name");

        assertEquals("Jan", result);
    }

    @Test
    void testSafeGetAsString_returnsNullForMissing() throws Exception {
        JsonObject obj = new JsonObject();

        Method method = ApiService.class.getDeclaredMethod("safeGetAsString", JsonObject.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(apiService, obj, "name");

        assertNull(result);
    }

    @Test
    void testSafeGetAsString_returnsNullForNonPrimitive() throws Exception {
        JsonObject obj = new JsonObject();
        obj.add("company", new JsonObject());

        Method method = ApiService.class.getDeclaredMethod("safeGetAsString", JsonObject.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(apiService, obj, "company");

        assertNull(result);
    }

    @Test
    void testFetchEmployeesFromApi_realApi() throws Exception {
        String apiUrl = "https://jsonplaceholder.typicode.com/users";

        List<Employee> employees = apiService.fetchEmployeesFromApi(apiUrl);

        Employee e = employees.get(0);

        assertAll(
                () -> assertFalse(employees.isEmpty(), "Lista pracowników nie powinna być pusta"),
                () -> assertNotNull(e.getFirstName(), "Imię pracownika nie powinno być null"),
                () -> assertNotNull(e.getEmail(), "Email pracownika nie powinien być null"),
                () -> assertEquals(Position.PROGRAMISTA, e.getPosition(),
                        "Pozycja powinna być ustawiona na PROGRAMISTA")
        );
    }

    @Test
    void testFetchEmployeesFromApi_invalidUrl() {
        String apiUrl = "https://jsonplaceholder.typicode.com/invalidEndpoint";

        ApiException ex = assertThrows(ApiException.class,
                () -> apiService.fetchEmployeesFromApi(apiUrl));

        assertAll(
                () -> assertNotNull(ex, "Powinien zostać rzucony ApiException"),
                () -> assertTrue(ex.getMessage().contains("Nieoczekiwany kod odpowiedzi HTTP"),
                        "Komunikat powinien zawierać informację o nieoczekiwanym kodzie HTTP")
        );
    }

    @Test
    void testFetchEmployeesFromApi_connectionErrorThrowsApiException() {
        ApiService apiService = new ApiService();
        String invalidUrl = "http://invalid.localhost:9999";

        ApiException ex = assertThrows(ApiException.class,
                () -> apiService.fetchEmployeesFromApi(invalidUrl));

        System.out.println("Message: " + ex.getMessage());

        assertAll(
                () -> assertNotNull(ex, "Powinien zostać rzucony ApiException"),
                () -> assertTrue(ex.getMessage().startsWith("B"),
                        "Komunikat powinien zaczynać się od litery B")
        );
    }
}
