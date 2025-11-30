package org.example.service;

import org.example.Interfaces.CalendarService;
import org.example.Interfaces.CompetenceRepository;
import org.example.Interfaces.ResourceAllocationService;
import org.example.Interfaces.Logger;
import org.example.model.Employee;

import java.util.List;

public class TaskAssignmentService {
    private final CalendarService calendar;
    private final CompetenceRepository competenceRepo;
    private final ResourceAllocationService allocationService;
    private final Logger logger;

    public TaskAssignmentService(CalendarService calendar,
                                 CompetenceRepository competenceRepo,
                                 ResourceAllocationService allocationService,
                                 Logger logger) {
        this.calendar = calendar;
        this.competenceRepo = competenceRepo;
        this.allocationService = allocationService;
        this.logger = logger;
    }

    public boolean assignTask(String taskId, List<String> requiredSkills, int estimatedTime) {
        for (Employee e : calendar.getAvailableEmployees()) {
            boolean hasAllSkills = requiredSkills.stream()
                    .allMatch(skill -> competenceRepo.hasCompetence(e.getEmail(), skill));
            if (hasAllSkills) {
                allocationService.recordAssignment(taskId, e.getEmail());
                logger.log("Assigned " + e.getEmail() + " to task " + taskId);
                return true;
            }
        }
        return false;
    }
}
