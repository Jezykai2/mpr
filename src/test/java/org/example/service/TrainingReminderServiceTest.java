package org.example.service;

import org.example.model.Employee;
import org.example.model.Position;
import org.example.service.TrainingReminderService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingReminderServiceTest {

    @Test
    void testSendReminders_sendsToEmployeesWithExpiringCertificates() {
        // Stub repozytorium certyfikatów – zwraca listę pracowników z datami wygaśnięcia
        CertificateStub certificateStub = new CertificateStub();
        certificateStub.addCertificate("jan@example.com", LocalDate.now().plusDays(10)); // wygasa za 10 dni
        certificateStub.addCertificate("anna@example.com", LocalDate.now().plusDays(40)); // wygasa za 40 dni

        // Spy serwisu komunikacji – rejestruje wysłane przypomnienia
        CommunicationSpy communicationSpy = new CommunicationSpy();

        // Mock serwisu komunikacji – weryfikuje liczbę wywołań
        CommunicationMock communicationMock = new CommunicationMock(1); // oczekujemy 1 wysyłki

        // Dummy logger – przekazywany do konstruktora, ale nieużywany
        LoggerDummy loggerDummy = new LoggerDummy();

        TrainingReminderService service = new TrainingReminderService(
                certificateStub, communicationSpy, communicationMock, loggerDummy
        );

        // Wywołanie logiki biznesowej
        service.sendReminders();

        // Weryfikacja – spy zarejestrował przypomnienie tylko dla Jana
        assertEquals(1, communicationSpy.getSentReminders().size());
        assertTrue(communicationSpy.getSentReminders().get(0).contains("jan@example.com"));

        // Mock sprawdza, że metoda wysyłki została wywołana dokładnie raz
        communicationMock.verify();
    }

    @Test
    void testSendReminders_noExpiringCertificates() {
        CertificateStub certificateStub = new CertificateStub();
        certificateStub.addCertificate("anna@example.com", LocalDate.now().plusDays(60)); // wygasa później

        CommunicationSpy communicationSpy = new CommunicationSpy();
        CommunicationMock communicationMock = new CommunicationMock(0); // oczekujemy 0 wysyłek
        LoggerDummy loggerDummy = new LoggerDummy();

        TrainingReminderService service = new TrainingReminderService(
                certificateStub, communicationSpy, communicationMock, loggerDummy
        );

        service.sendReminders();

        // Spy nie powinien zarejestrować żadnych przypomnień
        assertTrue(communicationSpy.getSentReminders().isEmpty());

        // Mock weryfikuje brak wywołań
        communicationMock.verify();
    }
}
