package org.example.service;

import org.example.Interfaces.CertificateRepository;
import org.example.Interfaces.CommunicationService;
import org.example.Interfaces.Logger;
import org.example.testing.doubles.CertificateStub;
import org.example.testing.doubles.CommunicationSpy;
import org.example.testing.doubles.CommunicationMock;
import org.example.testing.doubles.LoggerDummy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainingReminderServiceTest {

    @Test
    void testSendReminders_sendsToEmployeesWithExpiringCertificates() {
        // Stub repozytorium certyfikatów – implementuje CertificateRepository
        CertificateRepository certificateStub = new CertificateStub();
        ((CertificateStub) certificateStub).addCertificate("jan@example.com", LocalDate.now().plusDays(10)); // wygasa za 10 dni
        ((CertificateStub) certificateStub).addCertificate("anna@example.com", LocalDate.now().plusDays(40)); // wygasa za 40 dni

        // Spy serwisu komunikacji – implementuje CommunicationService
        CommunicationService communicationSpy = new CommunicationSpy();

        // Mock serwisu komunikacji – implementuje CommunicationService
        CommunicationMock communicationMock = new CommunicationMock(1); // oczekujemy 1 wysyłki

        // Dummy logger – implementuje Logger
        Logger loggerDummy = new LoggerDummy();

        TrainingReminderService service = new TrainingReminderService(
                certificateStub, communicationSpy, loggerDummy
        );

        // Wywołanie logiki biznesowej
        service.sendReminders();

        // Weryfikacja
        assertAll(
                () -> assertEquals(1, ((CommunicationSpy) communicationSpy).getSentReminders().size(),
                        "Powinno być dokładnie jedno przypomnienie"),
                () -> assertTrue(((CommunicationSpy) communicationSpy).getSentReminders().get(0).contains("jan@example.com"),
                        "Przypomnienie powinno być wysłane do Jana"),
                () -> {
                    communicationMock.sendReminder("jan@example.com", "Twój certyfikat wygasa " + LocalDate.now().plusDays(10));
                    communicationMock.verify();
                }
        );
    }

    @Test
    void testSendReminders_noExpiringCertificates() {
        CertificateRepository certificateStub = new CertificateStub();
        ((CertificateStub) certificateStub).addCertificate("anna@example.com", LocalDate.now().plusDays(60)); // wygasa później

        CommunicationService communicationSpy = new CommunicationSpy();
        CommunicationMock communicationMock = new CommunicationMock(0); // oczekujemy 0 wysyłek
        Logger loggerDummy = new LoggerDummy();

        TrainingReminderService service = new TrainingReminderService(
                certificateStub, communicationSpy, loggerDummy
        );

        service.sendReminders();

        // Weryfikacja
        assertAll(
                () -> assertTrue(((CommunicationSpy) communicationSpy).getSentReminders().isEmpty(),
                        "Nie powinno być żadnych przypomnień"),
                () -> communicationMock.verify()
        );
    }
}
