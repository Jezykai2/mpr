package org.example.testing.doubles;

import java.util.ArrayList;
import java.util.List;

public class CommunicationSpy {
    private final List<String> sentReminders = new ArrayList<>();

    public void sendReminder(String email, String message) {
        sentReminders.add("Reminder to " + email + ": " + message);
    }

    public List<String> getSentReminders() {
        return sentReminders;
    }
}
