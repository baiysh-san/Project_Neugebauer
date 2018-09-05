package pti.test;

import com.google.ortools.constraintsolver.*;

import java.util.*;

public class EmployeeScheduling {
    private int numWorkers;
    private int numMachines;
    private List<List<Integer>> availableMachines;
    private final int numHours;
    private final int maxAllowedHoursForWorker;
    private int actualNumWorkers;
    private int actualNumMachines;
    private List<List<Integer>> results;

    public EmployeeScheduling(int numWorkers, int numMachines,
                              int numHours, int maxAllowedHoursForWorker) {
        this.numWorkers = numWorkers;
        this.numMachines = numMachines;
        this.numHours = numHours;
        this.maxAllowedHoursForWorker = maxAllowedHoursForWorker;

        actualNumWorkers = numWorkers;
        actualNumMachines = numMachines;
        results = new ArrayList<>();
        for (int i = 0; i < actualNumWorkers; i++) {
            results.add(new ArrayList<>());
        }
    }

    static { System.loadLibrary("jniortools"); }

    public List<List<Integer>> getResults() {
        return results;
    }

    public void run(List<List<Integer>> availableMachines) {

        Solver solver = new Solver("schedule workers");

        if (numMachines < numWorkers) {
            numMachines = numWorkers;
        }

        for (int i = actualNumMachines; i < numMachines; i++) {
            List<Integer> scheduleRow = new ArrayList<>();
            for (int j = 0; j < numHours; j++) {
                scheduleRow.add(0);
            }
            availableMachines.add(scheduleRow);
        }

        List<List<Integer>> availableMachinesForHours = new ArrayList<>();
        for (int i = 0; i < numHours; i++) {
            availableMachinesForHours.add(new ArrayList<>());
        }

        int occupiedHours = 0;
        for (int i = 0; i < numMachines; i++) {
            for (int j = 0; j < numHours; j++) {
                if (availableMachines.get(i).get(j) == 1) {
                    occupiedHours++;
                    availableMachinesForHours.get(j).add(i);
                }
            }
        }
//        System.out.println("Occupied hours:" + occupiedHours + "\n");

        int maxMachinesSimultaneously = 0;
        for (List<Integer> availableMachinesFor : availableMachinesForHours) {
            int size = availableMachinesFor.size();
            if (maxMachinesSimultaneously < size) {
                maxMachinesSimultaneously = size;
            }
        }

        if (maxMachinesSimultaneously > numWorkers) {
            System.out.println("Workers count is not sufficient.");
            return;
        }

        if (numWorkers < numMachines) {
            numWorkers = numMachines;
        }

//        List<Integer> hoursForWorker = new ArrayList<>();
        List<List<IntVar>> machines = new ArrayList<>();
        List<IntVar> machinesFlat = new ArrayList<>();
        for (int j = 0; j < numWorkers; j++) {
//            hoursForWorker.add(0);
            List<IntVar> machine = new ArrayList<>();
            for (int i = 0; i < numHours; i++) {
                machine.add(solver.makeIntVar(0, numMachines-1, String.format("machines(%d,%d)", j, i)));
            }
            machines.add(machine);
            machinesFlat.addAll(machine);
        }

        if (occupiedHours / numWorkers <= maxAllowedHoursForWorker) {
//            int fullHoursCount = occupiedHours / maxAllowedHoursForWorker;
//            int restHours = occupiedHours % maxAllowedHoursForWorker;
//            int occupiedWorkers = fullHoursCount;
//            if (restHours > 0) {
//                occupiedWorkers++;
//            }
//
//            if (occupiedWorkers >= maxMachinesSimultaneously) {
//                for (int i = 0; i < fullHoursCount; i++) {
//                    hoursForWorker.set(i, maxAllowedHoursForWorker);
//                }
//                if (restHours > 0) {
//                    hoursForWorker.set(fullHoursCount, restHours);
//                }
//            } else {
//                int equalHours = occupiedHours / maxMachinesSimultaneously;
//                restHours = occupiedHours % maxMachinesSimultaneously;
//                for (int i = 0; i < maxMachinesSimultaneously; i++) {
//                    int arrangedHours = equalHours;
//                    if (restHours > 0) {
//                        arrangedHours++;
//                        restHours--;
//                    }
//
//                    hoursForWorker.set(i, arrangedHours);
//                }
//            }
        } else {
            System.out.println("Workers count is not sufficient.");
            return;
        }

        List<Map<Integer, IntVar>> workers = new ArrayList<>();
        for (int j = 0; j < numMachines; j++) {
            Map<Integer, IntVar> workerMap = new HashMap<>();
            for (int i = 0; i < numHours; i++) {
                workerMap.put(i, solver.makeIntVar(0, numWorkers - 1, String.format("machine%d hour%d", j, i)));
            }
            workers.add(workerMap);
        }

        for (int hour = 0; hour < numHours; hour++) {
            List<IntVar> workersForHour = new ArrayList<>();
            for (int j = 0; j < numMachines; j++) {
                workersForHour.add(workers.get(j).get(hour));
            }

            int workersForHourSize = workersForHour.size();
            for (int j = 0; j < numWorkers; j++) {
                IntVar s = machines.get(j).get(hour);
                solver.addConstraint(solver.MakeIndexOfConstraint(workersForHour.toArray(new IntVar[workersForHourSize]), s, j));
            }
        }

        for (int i = 0; i < numHours; i++) {
            List<IntVar> machinesPart = new ArrayList<>();
            for (int j = 0; j < numWorkers; j++) {
                machinesPart.add(machines.get(j).get(i));
            }
            solver.addConstraint(solver.makeAllDifferent(machinesPart.toArray(new IntVar[numWorkers])));

            List<IntVar> workersPart = new ArrayList<>();
            for (int j = 0; j < numMachines; j++) {
                workersPart.add(workers.get(j).get(i));
            }
            int workersPartSize = workersPart.size();
            solver.addConstraint(solver.makeAllDifferent(workersPart.toArray(new IntVar[workersPartSize])));
        }

        for (int j = 0; j < numWorkers; j++) {
            List<IntVar> machinesParts = new ArrayList<>();
            for (int i = 0; i < numHours; i++) {
                IntVar var = machines.get(j).get(i);
                List<IntVar> availableMachinesParts = new ArrayList<>();
                for (Integer availableMachine : availableMachinesForHours.get(i)) {
                    availableMachinesParts.add(solver.makeEquality(var, availableMachine).var());
                }
                int availableMachinesPartsSize = availableMachinesParts.size();
                if (availableMachinesPartsSize > 0) {
                    machinesParts.add(solver.makeMax(availableMachinesParts.toArray(new IntVar[availableMachinesPartsSize])).var().IsEqual(1));
                } else {
                    machinesParts.add(var.IsLessOrEqual(-1));
                }
            }

//            solver.addConstraint(solver.makeSumEquality(machinesParts.toArray(new IntVar[numHours]), hoursForWorker.get(j)));
            solver.addConstraint(solver.makeSumLessOrEqual(machinesParts.toArray(new IntVar[numHours]), maxAllowedHoursForWorker));
        }

        DecisionBuilder db = solver.makePhase(
                machinesFlat.toArray(new IntVar[numHours *numWorkers]),
                Solver.CHOOSE_FIRST_UNBOUND,
                Solver.ASSIGN_MIN_VALUE
        );
        Assignment solution = solver.makeAssignment();
        solution.add(machinesFlat.toArray(new IntVar[numHours *numWorkers]));
        SolutionCollector collector = solver.makeAllSolutionCollector(solution);

        db = solver.makeSolveOnce(db);
        if (solver.solve(db, new SearchMonitor[]{collector})) {
            int solutionCount = collector.solutionCount();
//            System.out.println("Solutions found: " + solutionCount);
//            System.out.println("Time: " + solver.wallTime() + "ms");
//            System.out.println();

            if (solutionCount > 0) {
//                System.out.println("Solution number " + 0 + "\n");

//                List<Map<Integer, Integer>> machinesMap = new ArrayList<>();
//                for (int i = 0; i < numMachines; i++) {
//                    machinesMap.add(new HashMap<>());
//                }

                for (int j = 0; j < actualNumWorkers; j++) {
//                    System.out.print("Worker  " + j + ": ");
                    for (int i = 0; i < numHours; i++) {
                        int machine = (int) collector.value(0, machines.get(j).get(i));
                        if (availableMachines.get(machine).get(i) == 1) {
//                            machinesMap.get(machine).put(i, j);
//                            System.out.print(machine + " ");
                            results.get(j).add(machine);
                        } else {
                            results.get(j).add(-1);
//                            System.out.print("_ ");
                        }
                    }
//                    System.out.println();
                }

//                System.out.println();
//                for (int i = 0; i < actualNumMachines; i++) {
//                    System.out.print("Machine " + i + ": ");
//                    for (int j = 0; j < numHours; j++) {
//                        if (machinesMap.get(i).containsKey(j)) {
//                            System.out.print(machinesMap.get(i).get(j) + " ");
//                        } else {
//                            System.out.print("_ ");
//                        }
//                    }
//                    System.out.println();
//                }
            }
        } else {
            System.out.println("No solution.");
        }
    }

    public static void main(String[] args) {
        int numWorkers = 4;
        int numMachines = 3;
        int numHours = 11;
        int maxAllowedHoursForWorker = 8;

        List<List<Integer>> availableMachines = new ArrayList<>();
        availableMachines.add(new ArrayList<>(Arrays.asList(1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1)));
        availableMachines.add(new ArrayList<>(Arrays.asList(1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0)));
        availableMachines.add(new ArrayList<>(Arrays.asList(0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1)));
//        availableMachines.add(new ArrayList<>(Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1)));

        EmployeeScheduling scheduling = new EmployeeScheduling(
                numWorkers,
                numMachines,
                numHours,
                maxAllowedHoursForWorker
        );
        scheduling.run(availableMachines);
    }
}