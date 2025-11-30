package org.example.testing.doubles;

import org.example.Interfaces.CompetenceRepository;
import java.util.*;

public class CompetenceFake implements CompetenceRepository {
    private final Map<String, List<String>> competences = new HashMap<>();

    public void addCompetence(String email, String skill) {
        competences.computeIfAbsent(email, k -> new ArrayList<>()).add(skill);
    }

    @Override
    public boolean hasCompetence(String email, String requiredSkill) {
        return competences.getOrDefault(email, List.of()).contains(requiredSkill);
    }
}
