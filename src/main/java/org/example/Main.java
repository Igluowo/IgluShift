package org.example;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shift.Employee;
import org.example.shift.Roster;
import org.example.shift.Shift;
import org.example.solver.RosterConstraintProvider;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Método auxiliar para cargar la lista desde el JSON
    private static List<Employee> loadEmployees(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(filePath), new TypeReference<List<Employee>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de empleados en: " + filePath, e);
        }
    }

    public static void main(String[] args) {

        SolverConfig solverConfig = new SolverConfig()
                .withSolutionClass(Roster.class)
                .withEntityClasses(Shift.class)
                .withConstraintProviderClass(RosterConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(3));

        SolverFactory<Roster> solverFactory = SolverFactory.create(solverConfig);
        Solver<Roster> solver = solverFactory.buildSolver();

        List<Employee> employees = loadEmployees("employees.json");

        List<Shift> shifts = new ArrayList<>();
        LocalDate monday = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);

            shifts.add(new Shift("Día " + (i+1) + " - Mañana",
                    LocalDateTime.of(day, LocalTime.of(10, 0)),
                    LocalDateTime.of(day, LocalTime.of(16, 0))));

            shifts.add(new Shift("Día " + (i+1) + " - Tarde",
                    LocalDateTime.of(day, LocalTime.of(14, 0)),
                    LocalDateTime.of(day, LocalTime.of(18, 0))));

            shifts.add(new Shift("Día " + (i+1) + " - Noche",
                    LocalDateTime.of(day, LocalTime.of(18, 0)),
                    LocalDateTime.of(day, LocalTime.of(23, 0))));
        }

        Roster firstSolution = new Roster(employees, shifts);

        System.out.println("Optimizando cuadrante con Timefold...");
        Roster solucion = solver.solve(firstSolution);

        System.out.println("\n==========================================");
        System.out.println("HORARIO GENERADO (Score: " + solucion.getScore() + ")");
        System.out.println("==========================================");
        for (Shift s : solucion.getShifts()) {
            System.out.println(s);
        }
    }
}