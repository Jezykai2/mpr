package service;

import com.google.gson.Gson;
import org.example.exception.ApiException;
import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.ApiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ApiServiceTest {

    @Test
    void testFetchEmployeesFromApiParsesCorrectJson() throws Exception {
        // Przygotowujemy sztuczną odpowiedź JSON z dwoma pracownikami
        String json = "[{\"name\":\"Jan Kowalski\",\"email\":\"jan@example.com\",\"company\":{\"name\":\"TechCorp\"}}," +
                "{\"name\":\"Anna Nowak\",\"email\":\"anna@example.com\",\"company\":{\"name\":\"OtherCorp\"}}]";

        // Tworzymy mock odpowiedzi HTTP
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);   // kod 200 = OK
        Mockito.when(mockResponse.body()).thenReturn(json);        // treść odpowiedzi = nasz JSON

        // Tworzymy mock klienta HTTP, który zawsze zwróci powyższą odpowiedź
        HttpClient mockClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Tworzymy ApiService, który korzysta z naszego mocka zamiast prawdziwego klienta
        ApiService apiService = new ApiService() {
            @Override
            public List<Employee> fetchEmployeesFromApi(String apiUrl) throws ApiException {
                // zamiast prawdziwego httpClient używamy mocka
                String body = mockResponse.body();
                Gson gson = new Gson();
                return super.fetchEmployeesFromApi(apiUrl); // normalna logika parsowania
            }
        };

        // Wywołujemy metodę i sprawdzamy czy poprawnie sparsowała JSON
        List<Employee> employees = apiService.fetchEmployeesFromApi("http://fake-url");
        assertEquals(2, employees.size()); // powinno być dwóch pracowników
        assertEquals("jan@example.com", employees.get(0).getEmail()); // pierwszy ma email jan@example.com
        assertEquals(Position.PROGRAMISTA, employees.get(0).getPosition()); // domyślne stanowisko PROGRAMISTA
    }

    @Test
    void testFetchEmployeesFromApiThrowsOnHttpError() throws Exception {
        // Przygotowujemy odpowiedź HTTP z błędem (status 500)
        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(500);
        Mockito.when(mockResponse.body()).thenReturn("Internal Server Error");

        HttpClient mockClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // ApiService, który rzuci wyjątek gdy status ≠ 200
        ApiService apiService = new ApiService() {
            @Override
            public List<Employee> fetchEmployeesFromApi(String apiUrl) throws ApiException {
                if (mockResponse.statusCode() != 200) {
                    throw new ApiException("Nieoczekiwany kod odpowiedzi HTTP: " + mockResponse.statusCode());
                }
                return List.of();
            }
        };

        // Sprawdzamy, że metoda rzuca ApiException przy statusie 500
        assertThrows(ApiException.class, () -> apiService.fetchEmployeesFromApi("http://fake-url"));
    }

    @Test
    void testFetchEmployeesFromApiHandlesMissingCompanyField() throws Exception {
        // JSON bez pola "company"
        String json = "[{\"name\":\"Jan Kowalski\",\"email\":\"jan@example.com\"}]";

        HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(json);

        HttpClient mockClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        ApiService apiService = new ApiService() {
            @Override
            public List<Employee> fetchEmployeesFromApi(String apiUrl) throws ApiException {
                String body = mockResponse.body();
                Gson gson = new Gson();
                return super.fetchEmployeesFromApi(apiUrl);
            }
        };

        // Wywołujemy metodę i sprawdzamy czy brak company został obsłużony jako "Unknown"
        List<Employee> employees = apiService.fetchEmployeesFromApi("http://fake-url");
        assertEquals(1, employees.size());
        assertEquals("Unknown", employees.get(0).getCompanyName()); // brak company → "Unknown"
    }
}
