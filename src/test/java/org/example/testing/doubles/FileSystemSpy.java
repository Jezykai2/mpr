package org.example.testing.doubles;

import java.util.ArrayList;
import java.util.List;

public class FileSystemSpy {
    public static class WriteOperation {
        private final String fileName;
        private final String content;

        public WriteOperation(String fileName, String content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() { return fileName; }
        public String getContent() { return content; }
    }

    private final List<WriteOperation> writes = new ArrayList<>();

    public void writeFile(String fileName, String content) {
        writes.add(new WriteOperation(fileName, content));
    }

    public List<WriteOperation> getWrites() {
        return writes;
    }
}
