package org.example.Interfaces;

import java.util.List;
import org.example.model.Employee;

public interface Formatter {
    String format(List<Employee> employees, String formatType);
}