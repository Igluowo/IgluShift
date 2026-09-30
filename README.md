# IgluShift ❄️⏱️

> Automated shift scheduling and roster optimization engine designed for retail and hospitality environments.

## Overview

Generating compliant, balanced, and fair shift schedules is an NP-hard combinatorial problem. In businesses with fluctuating traffic (e.g., event spikes, cruise arrivals) and varied employment contracts, manual scheduling frequently results in legal infractions or unfair shift distribution.

**IgluShift** solves this challenge by modeling the rostering problem using constraint satisfaction algorithms to deliver mathematically optimal schedules in seconds.

---

## Key Features (Current MVP)

- **Hard Legal Constraints (Compliance):**
  - **Single Shift per Day:** Prevents assigning more than one shift per calendar day to the same employee.
  - **Mandatory Daily Rest:** Enforces a minimum of 12 consecutive hours of rest between consecutive shifts.
  - **Weekly Work Limit:** Strictly caps assignments at a maximum of 5 working days per week.
  - **Consecutive Rest Days:** Enforces weekly rest periods in continuous blocks (no isolated single days off).

- **Soft Optimization Constraints (Fairness & Efficiency):**
  - **Contract Hours Balancing:** Dynamically minimizes deviation between scheduled hours and contract hours using quadratic penalties.
  - **Closing Shift Distribution:** Balances undesirable closing shifts equitably across the eligible team.

- **Data Decoupling:**
  - Employee rosters and contract specifications are externalized via JSON configurations (`employees.json`), keeping sensitive business and payroll data outside the codebase.

---

## Tech Stack

- **Language:** Java 21 (LTS)
- **Optimization Engine:** [Timefold Solver](https://timefold.ai/) (Community Edition)
- **Build Tool:** Apache Maven
- **Data Serialization:** Jackson Databind

---

## Architecture & Domain Model

- `Employee`: Planning problem fact representing staff members and contract capacities.
- `Shift`: Planning entity holding the `@PlanningVariable` (assigned `Employee`).
- `Roster`: Contained `@PlanningSolution` evaluating candidate schedules.
- `RosterConstraintProvider`: Implementation of the Constraint Streams API enforcing legal and business rules.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.9+

### Installation & Run

1. Clone the repository:
   ```bash
   git clone [https://github.com/tu-usuario/IgluShift.git](https://github.com/tu-usuario/IgluShift.git)
   cd IgluShift

2. Create your local employee configuration from the template:
   `cp employees.example.json employees.json`

3. Build and execute
   `mvn clean compile exec:java -Dexec.mainClass="com.horarios.Main"`

### Roadmap

- [ ] Support for explicit employee time-off and vacation requests.
- [ ] Shift generation based on point-of-sale (POS) foot-traffic peaks.
- [ ] Excel schedule export (.xlsx) via Apache POI.
- [ ] REST API integration using Spring Boot.

### License
This repository is publicly available for portfolio, review, and demonstration purposes only. All rights reserved. Commercial use, reproduction, or deployment without explicit authorization is strictly prohibited.
