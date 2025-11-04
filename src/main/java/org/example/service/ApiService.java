package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.exception.ApiException;
import org.example.model.Employee;
import org.example.model.Position;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Employee> fetchEmployeesFromApi(String apiUrl) throws ApiException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ApiException("Błąd połączenia z API: " + e.getMessage(), e);
        }

        int status = response.statusCode();
        if (status < 200 || status >= 300) {
            throw new ApiException("Nieoczekiwany kod odpowiedzi HTTP: " + status);
        }

        String body = response.body();
        List<Employee> result = new ArrayList<>();

        try {
            JsonElement je = gson.fromJson(body, JsonElement.class);
            if (!je.isJsonArray()) {
                throw new ApiException("Oczekiwano tablicy JSON z API.");
            }

            JsonArray arr = je.getAsJsonArray();
            for (JsonElement elem : arr) {
                if (!elem.isJsonObject()) continue;
                JsonObject obj = elem.getAsJsonObject();

                // pobieramy name, email, company.name
                String fullName = safeGetAsString(obj, "name");
                String email = safeGetAsString(obj, "email");

                String companyName = null;
                if (obj.has("company") && obj.get("company").isJsonObject()) {
                    JsonObject comp = obj.getAsJsonObject("company");
                    companyName = safeGetAsString(comp, "name");
                }
                if (companyName == null || companyName.trim().isEmpty()) {
                    companyName = "Unknown";
                }

                String firstName = "";
                String lastName = "";
                if (fullName != null) {
                    fullName = fullName.trim();
                    int lastSpace = fullName.lastIndexOf(' ');
                    if (lastSpace <= 0) {
                        firstName = fullName;
                        lastName = "";
                    } else {
                        firstName = fullName.substring(0, lastSpace).trim();
                        lastName = fullName.substring(lastSpace + 1).trim();
                    }
                } else {
                    firstName = "Unknown";
                    lastName = "";
                }

                Position pos = Position.PROGRAMISTA;
                double salary = pos.getBaseSalary();

                Employee e = new Employee(firstName, lastName, email, companyName, pos, salary);
                result.add(e);
            }

        } catch (Exception ex) {
            throw new ApiException("Błąd parsowania odpowiedzi z API: " + ex.getMessage(), ex);
        }

        return result;
    }

    private String safeGetAsString(JsonObject obj, String memberName) {
        if (obj.has(memberName) && obj.get(memberName).isJsonPrimitive()) {
            try {
                return obj.get(memberName).getAsString();
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }
}
