package org.example.testing.doubles;

public class FileSystemMock {
    private final String expectedFileName;
    private final String expectedContent;
    private boolean called = false;

    public FileSystemMock(String expectedFileName, String expectedContent) {
        this.expectedFileName = expectedFileName;
        this.expectedContent = expectedContent;
    }

    public void writeFile(String fileName, String content) {
        if (!fileName.equals(expectedFileName) || !content.equals(expectedContent)) {
            throw new AssertionError("FileSystemMock: wrong parameters");
        }
        called = true;
    }

    public void verify() {
        if (!called) {
            throw new AssertionError("FileSystemMock: expected writeFile to be called");
        }
    }
}
