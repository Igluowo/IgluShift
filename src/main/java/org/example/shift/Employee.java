package org.example.shift;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

public class Employee {

    @PlanningId
    private String name;
    private int weeklyContractHours;

    public Employee() {}

    public Employee(String name, int weeklyContractHours) {
        this.name = name;
        this.weeklyContractHours = weeklyContractHours;
    }

    public String getName() {
        return name;
    }

    public int getWeeklyContractHours() {
        return weeklyContractHours;
    }

    @Override
    public String toString() { return name; }
}
