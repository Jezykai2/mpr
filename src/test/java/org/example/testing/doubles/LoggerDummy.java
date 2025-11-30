package org.example.testing.doubles;

import org.example.Interfaces.Logger;

public class LoggerDummy implements Logger {
    @Override
    public void log(String message) {
        // Dummy – nic nie robi
    }
}
