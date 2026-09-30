package org.example.shift;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeConstraint {
    private DayOfWeek dayOfWeek;
    private LocalTime unavailableFrom;
    private LocalTime unavailableTo;

    public TimeConstraint() {}

    public TimeConstraint (DayOfWeek dayOfWeek, LocalTime unavailableFrom, LocalTime unavailableTo) {
        this.dayOfWeek = dayOfWeek;
        this.unavailableFrom = unavailableFrom;
        this.unavailableTo = unavailableTo;
    }

    public boolean overlaps(DayOfWeek shiftDay, LocalTime shiftStart, LocalTime shiftEnd) {
        if (!this.dayOfWeek.equals(shiftDay)) {
            return false;
        }
        return shiftStart.isBefore(this.unavailableTo) && shiftEnd.isAfter(this.unavailableFrom);
    }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getUnavailableFrom() { return unavailableFrom; }
    public void setUnavailableFrom(LocalTime unavailableFrom) { this.unavailableFrom = unavailableFrom; }

    public LocalTime getUnavailableTo() { return unavailableTo; }
    public void setUnavailableTo(LocalTime unavailableTo) { this.unavailableTo = unavailableTo; }
}
