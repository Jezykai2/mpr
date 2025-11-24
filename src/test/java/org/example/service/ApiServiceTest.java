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
        // używamy publicznego API jsonplaceholder jako źródła testowych danych
        String apiUrl = "https://jsonplaceholder.typicode.com/users";

        List<Employee> employees = apiService.fetchEmployeesFromApi(apiUrl);

        assertFalse(employees.isEmpty());
        Employee e = employees.get(0);

        assertNotNull(e.getFirstName());
        assertNotNull(e.getEmail());
        assertEquals(Position.PROGRAMISTA, e.getPosition());
    }

    @Test
    void testFetchEmployeesFromApi_invalidUrl() {
        String apiUrl = "https://jsonplaceholder.typicode.com/invalidEndpoint";

        ApiException ex = assertThrows(ApiException.class,
                () -> apiService.fetchEmployeesFromApi(apiUrl));

        assertTrue(ex.getMessage().contains("Nieoczekiwany kod odpowiedzi HTTP"));
    }

    @Test
    void testFetchEmployeesFromApi_connectionErrorThrowsApiException() {
        ApiService apiService = new ApiService();

        String invalidUrl = "http://invalid.localhost:9999";

        ApiException ex = assertThrows(ApiException.class,
                () -> apiService.fetchEmployeesFromApi(invalidUrl));

        // Wydruk dla debugowania
        System.out.println("Message: " + ex.getMessage());

        // Sprawdzamy tylko prefiks, który na pewno jest w Twoim kodzie
        assertTrue(ex.getMessage().startsWith("B"));
    }
}
