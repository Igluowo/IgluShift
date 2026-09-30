package org.example.shift;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {

    @PlanningId
    private String name;
    private int weeklyContractHours;
    private Set<LocalDate> unavailableDates = new HashSet<>();
    private List<TimeConstraint> recurringTimeConstraints = new ArrayList<>();

    public Employee() {}

    public Employee(String name, int weeklyContractHours) {
        this.name = name;
        this.weeklyContractHours = weeklyContractHours;
    }

    public Employee(String name, int weeklyContractHours, Set<LocalDate> unavailableDates,
                    List<TimeConstraint> recurringTimeConstraints) {
        this.name = name;
        this.weeklyContractHours = weeklyContractHours;
        this.unavailableDates = unavailableDates != null ? unavailableDates : new HashSet<>();
        this.recurringTimeConstraints = recurringTimeConstraints;
    }

    public boolean isUnavailable(LocalDate date) {
        return unavailableDates != null && unavailableDates.contains(date);
    }

    public boolean isTimeRestricted (LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        if (recurringTimeConstraints == null || recurringTimeConstraints.isEmpty()) {
            return false;
        }
        return recurringTimeConstraints.stream().anyMatch(tc ->
                tc.overlaps(shiftStart.getDayOfWeek(), shiftStart.toLocalTime(), shiftEnd.toLocalTime()));
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public int getWeeklyContractHours() {
        return weeklyContractHours;
    }
    public void setWeeklyContractHours(int weeklyContractHours) { this.weeklyContractHours = weeklyContractHours; }

    public Set<LocalDate> getUnavailableDates() { return unavailableDates; }
    public void setUnavailableDates(Set<LocalDate> unavailableDates) {
        this.unavailableDates = unavailableDates != null ? unavailableDates : new HashSet<>();
    }

    public List<TimeConstraint> getRecurringTimeConstraints() {
        return recurringTimeConstraints;
    }

    public void setRecurringTimeConstraints(List<TimeConstraint> recurringTimeConstraints) {
        this.recurringTimeConstraints = recurringTimeConstraints != null ? recurringTimeConstraints : new ArrayList<>();
    }

    @Override
    public String toString() { return name; }
}
