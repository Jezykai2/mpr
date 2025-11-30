package org.example.testing.doubles;

import org.example.Interfaces.CertificateRepository;
import java.time.LocalDate;
import java.util.*;

public class CertificateStub implements CertificateRepository {
    private final Map<String, LocalDate> certificates = new HashMap<>();

    public void addCertificate(String email, LocalDate expiryDate) {
        certificates.put(email, expiryDate);
    }

    @Override
    public Map<String, LocalDate> getCertificates() {
        return certificates;
    }
}
