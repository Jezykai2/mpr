package org.example.testing.doubles;

import org.example.Interfaces.CommunicationService;

public class CommunicationMock implements CommunicationService {
    private final int expectedCalls;
    private int actualCalls = 0;

    public CommunicationMock(int expectedCalls) {
        this.expectedCalls = expectedCalls;
    }

    @Override
    public void sendReminder(String email, String message) {
        actualCalls++;
    }

    public void verify() {
        if (actualCalls != expectedCalls) {
            throw new AssertionError("Expected " + expectedCalls + " calls, but got " + actualCalls);
        }
    }
}
