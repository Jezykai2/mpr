package org.example.testing.doubles;

public class FormatterStub {
    private final String predefinedOutput;

    public FormatterStub(String predefinedOutput) {
        this.predefinedOutput = predefinedOutput;
    }

    public String format(Object data, String formatType) {
        return predefinedOutput;
    }
}
