package org.example.testing.doubles;

public class CommunicationMock {
    private final int expectedCalls;
    private int actualCalls = 0;

    public CommunicationMock(int expectedCalls) {
        this.expectedCalls = expectedCalls;
    }

    public void sendReminder(String email, String message) {
        actualCalls++;
    }

    public void verify() {
        if (actualCalls != expectedCalls) {
            throw new AssertionError("Expected " + expectedCalls + " calls, but got " + actualCalls);
        }
    }
}
