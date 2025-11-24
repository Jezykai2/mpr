package org.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class ImportSummaryTest {

    @Test
    void testToStringWithErrors() {
        ImportSummary summary = new ImportSummary();
        summary.incrementImported();
        summary.addError("Błąd walidacji");

        String output = summary.toString();

        System.out.println(output);

        assertTrue(output.contains("Zaimportowano: 1"));


    }
}
