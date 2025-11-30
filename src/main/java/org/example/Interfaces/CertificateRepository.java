package org.example.Interfaces;

import java.time.LocalDate;
import java.util.Map;

public interface CertificateRepository {
    Map<String, LocalDate> getCertificates();
}