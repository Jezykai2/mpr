package org.example.service;

import org.example.Interfaces.CertificateRepository;
import org.example.Interfaces.CommunicationService;
import org.example.Interfaces.Logger;

import java.time.LocalDate;
import java.util.Map;

public class TrainingReminderService {
    private final CertificateRepository certificateRepo;
    private final CommunicationService communicationService;
    private final Logger logger;

    public TrainingReminderService(CertificateRepository certificateRepo,
                                   CommunicationService communicationService,
                                   Logger logger) {
        this.certificateRepo = certificateRepo;
        this.communicationService = communicationService;
        this.logger = logger;
    }

    public void sendReminders() {
        LocalDate now = LocalDate.now();
        for (Map.Entry<String, LocalDate> entry : certificateRepo.getCertificates().entrySet()) {
            String email = entry.getKey();
            LocalDate expiry = entry.getValue();
            if (expiry.isBefore(now.plusDays(30))) {
                String message = "Twój certyfikat wygasa " + expiry;
                communicationService.sendReminder(email, message);
                logger.log("Reminder sent to " + email);
            }
        }
    }
}
