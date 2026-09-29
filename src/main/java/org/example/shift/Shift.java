package org.example.shift;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;

import java.time.Duration;
import java.time.LocalDateTime;

@PlanningEntity
public class Shift {

    @PlanningId
    private String id;
    private LocalDateTime start;
    private LocalDateTime end;

    @PlanningVariable
    private Employee employee;

    public Shift() {}

    public Shift(String id, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public long getDurationInHours() {
        return Duration.between(start, end).toHours();
    }

    public String getId() { return id; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    @Override
    public String toString() {
        return id + " (" + start.toLocalDate() + " " + start.toLocalTime() + "-" + end.toLocalTime() +
                ") -> " + (employee != null ? employee.getName() : "SIN ASIGNAR");
    }
}
