package org.example.testing.doubles;

import org.example.Interfaces.ResourceAllocationService;
import java.util.ArrayList;
import java.util.List;

public class ResourceAllocationSpy implements ResourceAllocationService {
    public static class Assignment {
        private final String taskId;
        private final String employeeEmail;

        public Assignment(String taskId, String employeeEmail) {
            this.taskId = taskId;
            this.employeeEmail = employeeEmail;
        }

        public String getTaskId() { return taskId; }
        public String getEmployeeEmail() { return employeeEmail; }
    }

    private final List<Assignment> assignments = new ArrayList<>();

    @Override
    public void recordAssignment(String taskId, String employeeEmail) {
        assignments.add(new Assignment(taskId, employeeEmail));
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
