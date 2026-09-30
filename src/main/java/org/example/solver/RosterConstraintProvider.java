package org.example.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.*;
import org.example.shift.Shift;

import java.time.Duration;

public class RosterConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[] {
                noOverlappingShifts(factory),
                minimiumRest12Hours(factory),
                max5DaysWorked(factory),
                consecutiveDaysOff(factory),
                employeeUnavailable(factory),
                fulfillContractHours(factory),
                balanceClosingShifts(factory),
                recurringTimeRestrictions(factory)
        };
    }

    private Constraint noOverlappingShifts(ConstraintFactory factory) {
        return factory.forEachUniquePair(Shift.class,
                Joiners.equal(Shift::getEmployee))
                .filter((s1, s2) -> s1.getStart().toLocalDate().equals(s2.getStart().toLocalDate()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Mismo empleado no puede doblar día");
    }

    private Constraint minimiumRest12Hours(ConstraintFactory factory) {
        return factory.forEachUniquePair(Shift.class,
                Joiners.equal(Shift::getEmployee))
                .filter((s1, s2) -> {
                    Duration r1 = Duration.between(s1.getEnd(), s2.getStart());
                    Duration r2 = Duration.between(s2.getEnd(), s1.getStart());
                    return (!r1.isNegative() && r1.toHours() < 12) ||
                            (!r2.isNegative() && r1.toHours() < 12);
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Descanso obligatorio de 12 horas");
    }

    private Constraint fulfillContractHours(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .groupBy(Shift::getEmployee, ConstraintCollectors.sumLong(Shift::getDurationInHours))
                .penalize(HardSoftScore.ONE_SOFT, (emp, totalHours) -> {
                    long diff = Math.abs(totalHours - emp.getWeeklyContractHours());
                    return (int) (diff * diff);
                })
                .asConstraint("Ajustar a horas del contrato");
    }

    private Constraint consecutiveDaysOff(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .join(Shift.class,
                        Joiners.equal(Shift::getEmployee),
                        Joiners.filtering((s1, s2) ->
                                s1.getStart().toLocalDate().plusDays(2).equals(s2.getStart().toLocalDate()))
                )
                .ifNotExists(Shift.class,
                        Joiners.filtering((s1, s2, midShift) ->
                                midShift.getEmployee() != null &&
                                        midShift.getEmployee().equals(s1.getEmployee()) &&
                                        midShift.getStart().toLocalDate().equals(s1.getStart().toLocalDate().plusDays(1)))
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Días libres deben ser consecutivos");
    }

    private Constraint max5DaysWorked(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .groupBy(Shift::getEmployee,
                        ConstraintCollectors.countDistinct(shift -> shift.getStart().toLocalDate()))
                .filter((employee, daysWorked) -> daysWorked > 5)
                .penalize(HardSoftScore.ONE_HARD, (employee, daysWorked) -> daysWorked - 5)
                .asConstraint("Maximo 5 dias trabajados");
    }

    private Constraint balanceClosingShifts(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getEnd().toLocalTime().isAfter(java.time.LocalTime.of(23, 0)))
                .groupBy(Shift::getEmployee, ConstraintCollectors.count())
                .penalize(HardSoftScore.ONE_SOFT, (employee, closingCount) -> closingCount * closingCount)
                .asConstraint("Reparto equitativo de cierres");
    }

    private Constraint employeeUnavailable(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getEmployee() != null &&
                        shift.getEmployee().isUnavailable(shift.getStart().toLocalDate()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Día no disponible");
    }

    private Constraint recurringTimeRestrictions(ConstraintFactory factory) {
        return factory.forEach(Shift.class)
                .filter(shift -> shift.getEmployee() != null &&
                        shift.getEmployee().isTimeRestricted(shift.getStart(), shift.getEnd()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Incompatibilidad horaria recurrente");
    }
}
