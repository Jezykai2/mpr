package org.example.testing.doubles;

// Dummy obiekt – tylko wypełniacz, nieużywany w testach
public class ConfigDummy {
    // brak logiki – jeśli ktoś wywoła metodę, rzucamy wyjątek
    public void unusedMethod() {
        throw new UnsupportedOperationException("Dummy obiekt – nie powinien być używany");
    }
}
