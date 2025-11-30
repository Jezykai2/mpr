package org.example.testing.doubles;

import org.example.Interfaces.Formatter;
import org.example.model.Employee;
import java.util.List;

public class FormatterStub implements Formatter {
    private final String predefinedOutput;

    public FormatterStub(String predefinedOutput) {
        this.predefinedOutput = predefinedOutput;
    }

    @Override
    public String format(List<Employee> employees, String formatType) {
        return predefinedOutput;
    }
}
