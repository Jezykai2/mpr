package org.example.testing.doubles;

import org.example.Interfaces.CommunicationService;
import java.util.ArrayList;
import java.util.List;

public class CommunicationSpy implements CommunicationService {
    private final List<String> sentReminders = new ArrayList<>();

    @Override
    public void sendReminder(String email, String message) {
        sentReminders.add("Reminder to " + email + ": " + message);
    }

    public List<String> getSentReminders() {
        return sentReminders;
    }
}
