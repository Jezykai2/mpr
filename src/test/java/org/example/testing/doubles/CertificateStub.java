package org.example.testing.doubles;

import java.time.LocalDate;
import java.util.*;

public class CertificateStub {
    private final Map<String, LocalDate> certificates = new HashMap<>();

    public void addCertificate(String email, LocalDate expiryDate) {
        certificates.put(email, expiryDate);
    }

    public Map<String, LocalDate> getCertificates() {
        return certificates;
    }
}
