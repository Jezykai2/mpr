package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ImportSummary {
    private int importedCount;
    private final List<String> errors = new ArrayList<>();

    public ImportSummary() { }

    public void incrementImported() {
        this.importedCount++;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public int getImportedCount() {
        return importedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Zaimportowano: ").append(importedCount).append("\n");
        if (errors.isEmpty()) {
            sb.append("Brak błędów.");
        } else {
            sb.append("Błędy:\n");
            for (String e : errors) {
                sb.append(" - ").append(e).append("\n");
            }
        }
        return sb.toString();
    }
}
