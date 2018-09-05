package pti.test;

import com.google.ortools.constraintsolver.*;
import org.springframework.context.support.GenericXmlApplicationContext;
import pti.model.domain.*;
import pti.model.service.EmployeeService;
import pti.model.service.MachineService;
import pti.model.service.OrderService;
import pti.model.service.ProductService;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class JobShop {
    static { System.loadLibrary("jniortools"); }

    private int workingDayBeginTime;
    private int workingDayDuration;
    private int breakDurationMinutes;
    private int hoursWithoutBreak;
    private int workingDayDurationWithoutBreaksMinutes;
    private int breaksCountPerDay;
    private List<Machine> allMachines;
    private List<Product> products;
    private List<List<Integer>> operationsOnMachines;
    private List<List<Integer>> processingTimes;
    private List<Employee> employees;
    private Map<Long, Integer> machinesIdMapping;
    private Map<Long, Integer> productsIdMapping;
    private List<Order> orders;
    private int minimalTimeUnitMinutes = 30;
    private int maxAllowedHoursForWorker;
    private List<List<Operation>> actualProductOperationsOrder;
    private OrderService orderService;

    public JobShop(int workingDayBeginTime,
                   int workingDayDuration,
                   int breakDurationMinutes,
                   int hoursWithoutBreak,
                   int maxAllowedHoursForWorker,
                   List<Machine> allMachines,
                   List<Employee> employees,
                   List<Order> orders,
                   OrderService orderService
    ) {
        this.workingDayBeginTime = workingDayBeginTime;
        this.workingDayDuration = workingDayDuration;
        this.breakDurationMinutes = breakDurationMinutes;
        this.hoursWithoutBreak = hoursWithoutBreak;
        this.maxAllowedHoursForWorker = maxAllowedHoursForWorker;
        this.allMachines = allMachines;
        this.employees = employees;
        this.orders = orders;
        this.products = new ArrayList<>();
        for (Order order : orders) {
            this.products.add(order.getProduct());
        }
        this.orderService = orderService;
        measureWorkingDayDurationWithoutBreaks();
        actualProductOperationsOrder = new ArrayList<>();
        for (int i = 0; i < this.products.size(); i++) {
            actualProductOperationsOrder.add(new ArrayList<>());
        }
        machinesIdMapping = new HashMap<>();
        productsIdMapping = new HashMap<>();
        for (int i = 0; i < allMachines.size(); i++) {
            machinesIdMapping.put(allMachines.get(i).getId(), i);
        }

        operationsOnMachines = new ArrayList<>();
        processingTimes = new ArrayList<>();
        for (int i = 0; i < this.products.size(); i++) {
            Product p = this.products.get(i);
            productsIdMapping.put(p.getId(), i);
            List<Integer> operationsForProduct = new ArrayList<>();
            List<Integer> durationsForProduct = new ArrayList<>();
            Operation currentOperation = p.getWorkschedule().getOperations().stream().filter(o -> o.getBeforeOperation() == null).collect(Collectors.toList()).get(0);
            for (int j = 0; j < p.getWorkschedule().getOperations().size(); j++) {
                actualProductOperationsOrder.get(i).add(currentOperation);
                operationsForProduct.add(machinesIdMapping.get(currentOperation.getMachine().getId()));
                durationsForProduct.add(currentOperation.getDuration());
                currentOperation = currentOperation.getAfterOperation();
            }
            operationsOnMachines.add(operationsForProduct);
            processingTimes.add(durationsForProduct);
        }
    }

    private void measureWorkingDayDurationWithoutBreaks() {
        int cycle = hoursWithoutBreak * 60 + breakDurationMinutes;
        breaksCountPerDay = workingDayDuration * 60 / cycle;
        int remaining = workingDayDuration * 60 % cycle;
        if (remaining <= hoursWithoutBreak * 60) {
            workingDayDurationWithoutBreaksMinutes = breaksCountPerDay * hoursWithoutBreak * 60 + remaining;
        } else {
            workingDayDurationWithoutBreaksMinutes = (breaksCountPerDay + 1) * hoursWithoutBreak * 60;
        }
    }

    private boolean isBreakMinute(long minute) {
        minute = minute % (24 * 60);
        for (int i = 0; i < breaksCountPerDay; i++) {
            int breakStart = workingDayBeginTime * 60 + hoursWithoutBreak * (i+1) * 60 + breakDurationMinutes * i;
            int breakEnd = workingDayBeginTime * 60 + hoursWithoutBreak * (i+1) * 60 + breakDurationMinutes * (i+1);
            if (breakEnd > minute && minute >= breakStart) {
                return true;
            }
        }

        return false;
    }

    private boolean isWorkingTime(long minute) {
        minute = minute % (24 * 60);

        return minute >= workingDayBeginTime * 60 && minute < (workingDayBeginTime + workingDayDuration) * 60;
    }

    public List<Order> run() {
        Solver solver = new Solver("JobShop");

        long horizon = 0;
        for (int i = 0; i < products.size(); i++) {
            horizon += processingTimes.get(i).stream().mapToInt(Integer::intValue).sum();
        }

        long maxStart = 0;
        List<Long> machinesStartHour = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime lowestDate = LocalDateTime.now();
        lowestDate = lowestDate.plusDays(1);
        lowestDate = LocalDateTime.of(lowestDate.getYear(), lowestDate.getMonth(), lowestDate.getDayOfMonth(), 0, 0);
        for (Machine machine : allMachines) {
            long start = 0;
            LocalDateTime machineOperativeDate = machine.getNotOperativeUntil();
            if (machineOperativeDate != null) {
                Duration diff = Duration.between(machineOperativeDate, currentDate);
                long diffHours = diff.toHours();
                if (diffHours >= 0) {
                    start = 0;
                } else {
                    if (lowestDate.isAfter(machineOperativeDate)) {
                        lowestDate = machineOperativeDate;
                    }
                    start = -diffHours;
                }
            }
            machinesStartHour.add(start);
            if (start > maxStart) {
                maxStart = start;
            }
        }
        horizon += maxStart;

        List<List<IntervalVar>> allTasks = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            List<IntervalVar> task = new ArrayList<>();
            for (int j = 0; j < operationsOnMachines.get(i).size(); j++) {
                task.add(solver.makeFixedDurationIntervalVar(
                        machinesStartHour.get(operationsOnMachines.get(i).get(j)), horizon,
                        processingTimes.get(i).get(j),
                        false, i + " " + j));
            }
            allTasks.add(task);
        }

        List<SequenceVar> allSequences = new ArrayList<>();

        for (int i = 0; i < allMachines.size(); i++) {
            List<IntervalVar> machinesJobs = new ArrayList<>();
            for (int j = 0; j < products.size(); j++) {
                for (int k = 0; k < operationsOnMachines.get(j).size(); k++) {
                    if (operationsOnMachines.get(j).get(k).equals(i)) {
                        machinesJobs.add(allTasks.get(j).get(k));
                    }
                }
            }

            IntervalVar temp[] = new IntervalVar[machinesJobs.size()];
            DisjunctiveConstraint disJ = solver.makeDisjunctiveConstraint(machinesJobs.toArray(temp), "machine " + i);
            allSequences.add(disJ.makeSequenceVar());
            solver.addConstraint(disJ);
        }

        for (int i = 0; i < products.size(); i++) {
            for (int j = 0; j < operationsOnMachines.get(i).size()-1; j++) {
                solver.addConstraint(
                        solver.makeIntervalVarRelation(
                                allTasks.get(i).get(j+1),
                                Solver.STARTS_AFTER_END,
                                allTasks.get(i).get(j)));
            }
        }

        List<IntExpr> intExprs = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            intExprs.add(allTasks.get(i).get(operationsOnMachines.get(i).size()-1).EndExpr());
        }

        IntVar[] intExprsArr = new IntVar[intExprs.size()];
        for (int i = 0; i < intExprs.size(); i++) {
            intExprsArr[i] = intExprs.get(i).var();
        }
        IntVar objVar = solver.makeMax(intExprsArr).var();
        OptimizeVar objectiveMonitor = solver.makeMinimize(objVar, 1);

        SequenceVar allSequencesArr[] = new SequenceVar[allMachines.size()];
        for (int i = 0; i < allMachines.size(); i++) {
            allSequencesArr[i] = allSequences.get(i);
        }

        DecisionBuilder sequencePhase = solver.makePhase(allSequencesArr, Solver.SEQUENCE_DEFAULT);
        DecisionBuilder varsPhase = solver.makePhase(new IntVar[]{objVar}, Solver.CHOOSE_FIRST_UNBOUND, Solver.ASSIGN_MIN_VALUE);
        DecisionBuilder mainPhase = solver.compose(new DecisionBuilder[] {sequencePhase, varsPhase});

        SolutionCollector collector = solver.makeLastSolutionCollector();
        collector.add(allSequencesArr);
        collector.addObjective(objVar);

        for (int i = 0; i < allMachines.size(); i++) {
            SequenceVar sequence = allSequences.get(i);
            long sequenceCount = sequence.size();
            for (int j = 0; j < sequenceCount; j++) {
                IntervalVar t = sequence.Interval(j);
                collector.add(t.StartExpr().var());
                collector.add(t.EndExpr().var());
            }
        }

        int dispColWidth = 15;

        if (solver.solve(mainPhase, new SearchMonitor[]{objectiveMonitor, collector})) {
            long maxHours = collector.objectiveValue(0);
            System.out.println("Optimal Schedule Length: " + maxHours);
            StringBuilder solLine = new StringBuilder();
            StringBuilder solLineTasks = new StringBuilder();
            System.out.println("Optimal Schedule:");

            List<List<BeginEndTask>> machinesScheduleStraightHours = new ArrayList<>();
            for (int i = 0; i < allMachines.size(); i++) {
                List<BeginEndTask> mT = new ArrayList<>();
                SequenceVar seq = allSequences.get(i);
                solLine.append("Machine ").append(i).append(": ");
                solLineTasks.append("Machine").append(i).append(": ");
                int sequence[] = collector.ForwardSequence(0, seq);

                for (int aSequence1 : sequence) {
                    IntervalVar t = seq.Interval(aSequence1);
                    solLineTasks.append(t.name()).append(repeat(" ", dispColWidth - t.name().length()));
                }

                for (int aSequence : sequence) {
                    IntervalVar t = seq.Interval(aSequence);
                    long startTime = collector.value(0, t.StartExpr().var());
                    long endTime = collector.value(0, t.EndExpr().var());
                    String[] res = t.name().split(" ");
                    mT.add(new BeginEndTask(Integer.parseInt(res[0]), Integer.parseInt(res[1]), i, startTime, endTime));
                    String solTmp = "[" + startTime + ",";
                    solTmp += endTime + "] ";
                    solLine.append(solTmp).append(repeat(" ", dispColWidth - solTmp.length()));
                }

                solLine.append("\n");
                solLineTasks.append("\n");
                machinesScheduleStraightHours.add(mT);
            }

            System.out.println(solLineTasks);
            System.out.println("Time Intervals for Tasks");
            System.out.println(solLine);

//            LocalDateTime startTime = LocalDateTime.of(2018, Month.JUNE, 10, 0, 0);

            long maxDays = maxHours * 60 / workingDayDurationWithoutBreaksMinutes;
            if (maxHours * 60 % workingDayDurationWithoutBreaksMinutes > 0) {
                maxDays++;
            }

            saveProductionOrderOperations(machinesScheduleStraightHours, lowestDate, maxDays);
        }

        return orders;
    }

    private void saveProductionOrderOperations(
            List<List<BeginEndTask>> machinesScheduleStraightHours,
            LocalDateTime startTime,
            long maxDays
    ) {
        List<List<BeginEndTask>> productsSchedule = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            productsSchedule.add(new ArrayList<>());
        }

        List<List<List<Integer>>> minutesAvailableForMachinesForDays = new ArrayList<>();
        for (int i = 0; i < maxDays; i++) {
            minutesAvailableForMachinesForDays.add(new ArrayList<>());
            for (int j = 0; j < allMachines.size(); j++) {
                minutesAvailableForMachinesForDays.get(i).add(new ArrayList<>());
            }
        }

        List<List<AvailabilityMapping>> minutesAvailableForMachinesFlattened = new ArrayList<>();
        List<List<AvailabilityMapping>> finalMapping = new ArrayList<>();

        int beginMinute = workingDayBeginTime * 60;
        for (int i = 0; i < machinesScheduleStraightHours.size(); i++) {
            List<AvailabilityMapping> minutesAvailable = new ArrayList<>();
            long lastOccupied = beginMinute;
            long prevEnd = -1;
            for (int j = 0; j < machinesScheduleStraightHours.get(i).size(); j++) {
                BeginEndTask task = machinesScheduleStraightHours.get(i).get(j);
                long startDay = task.start * 60 / workingDayDurationWithoutBreaksMinutes;
                long rem = task.start * 60 % workingDayDurationWithoutBreaksMinutes;
                long startCycle = rem / (hoursWithoutBreak * 60);
                rem = rem % (hoursWithoutBreak * 60);
                long start = startDay * 24 * 60 + workingDayBeginTime * 60 + startCycle * (hoursWithoutBreak * 60 + breakDurationMinutes) + rem;

                long endDay = task.end * 60 / workingDayDurationWithoutBreaksMinutes;
                rem = task.end * 60 % workingDayDurationWithoutBreaksMinutes;
                long end;
                if (rem == 0) {
                    end = (endDay-1) * 24 * 60 + workingDayBeginTime * 60 + workingDayDuration * 60;
                } else {
                    long endCycle = rem / (hoursWithoutBreak * 60);
                    rem = rem % (hoursWithoutBreak * 60);
                    end = endDay * 24 * 60 + workingDayBeginTime * 60 + endCycle * (hoursWithoutBreak * 60 + breakDurationMinutes) + rem;
                }

                for (long k = lastOccupied; k < start; k += minimalTimeUnitMinutes) {
                    if (isWorkingTime(k) && prevEnd != task.start) {
                        minutesAvailable.add(new AvailabilityMapping(-1, i, -1, 0, k));
                    }
                }

                for (long k = start; k < end; k += minimalTimeUnitMinutes) {
                    if (isWorkingTime(k)) {
                        if (isBreakMinute(k)) {
                            minutesAvailable.add(new AvailabilityMapping(task.operation, i, task.product, 0, k));
                        } else {
                            minutesAvailable.add(new AvailabilityMapping(task.operation, i, task.product, 1, k));
                        }
                    }
                }
                lastOccupied = end;
                prevEnd = task.end;

                productsSchedule.get(task.product).add(new BeginEndTask(task.product, task.operation, task.machine,
                        start, end));
            }
            minutesAvailableForMachinesFlattened.add(minutesAvailable);
        }

        for (int i = 0; i < minutesAvailableForMachinesFlattened.size(); i++) {
            int currentDay = -1;
            for (int j = 0; j < minutesAvailableForMachinesFlattened.get(i).size(); j++) {
                if (j % (workingDayDuration * 2) == 0) {
                    currentDay++;
                }
                minutesAvailableForMachinesForDays.get(currentDay).get(i).add(minutesAvailableForMachinesFlattened.get(i).get(j).occupied);
            }
        }

        for (List<List<Integer>> lst1 : minutesAvailableForMachinesForDays) {
            int i = 0;
            for (List<Integer> lst2 : lst1) {
                if (lst2.size() < workingDayDuration * 2) {
                    int difference = workingDayDuration * 2 - lst2.size();
                    for (int k = 0; k < difference; k++) {
                        lst2.add(0);
                        minutesAvailableForMachinesFlattened.get(i).add(new AvailabilityMapping(-1, -1, -1, 0, 0));
                    }
                }
                i++;
            }
        }

        EmployeeScheduling scheduling = new EmployeeScheduling(
                employees.size(),
                allMachines.size(),
                workingDayDuration*2,
                maxAllowedHoursForWorker*2
        );

        for (List<List<Integer>> lst1 : minutesAvailableForMachinesForDays) {
            scheduling.run(lst1);
        }

        List<List<Integer>> results = scheduling.getResults();
        for (int j = 0; j < results.size(); j++) {
            for (int i = 0; i < results.get(j).size(); i++) {
                int machineForWorker = results.get(j).get(i);
                if (machineForWorker >= 0) {
                    minutesAvailableForMachinesFlattened.get(machineForWorker).get(i).worker = j;
                }
            }
        }

        List<List<List<AvailabilityMapping>>> productsFinalMapping = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            productsFinalMapping.add(new ArrayList<>());
            for (int j = 0; j < products.get(i).getWorkschedule().getOperations().size(); j++) {
                productsFinalMapping.get(i).add(new ArrayList<>());
            }
        }

        for (List<AvailabilityMapping> lst : minutesAvailableForMachinesFlattened) {
            for (AvailabilityMapping m : lst) {
                if (m.product >= 0) {
                    productsFinalMapping.get(m.product).get(m.operation).add(m);
                }
            }
        }

        for (List<BeginEndTask> ps : productsSchedule) {
            ps.sort((o1, o2) -> {
                if (o1.operation > o2.operation) {
                    return 1;
                } else if (o1.operation < o2.operation) {
                    return -1;
                }

                return 0;
            });
        }

        for (Order order : orders) {
            int pId = productsIdMapping.get(order.getProduct().getId());

            for (int i = 0; i < productsSchedule.get(pId).size(); i++) {
                ProductionOrder productionOrder = order.getProductionOrder();

                long start = productsSchedule.get(pId).get(i).start;
                long end = productsSchedule.get(pId).get(i).end;
                int operation = productsSchedule.get(pId).get(i).operation;
                int machine = productsSchedule.get(pId).get(i).machine;
                ProductionOrderOperation productionOrderOperation = new ProductionOrderOperation();
                productionOrderOperation.setBeginn(startTime.plusMinutes(start));
                productionOrderOperation.setEnd(startTime.plusMinutes(end));

                productionOrder.getProductionOrderOperations().add(productionOrderOperation);
                productionOrderOperation.setProductionOrder(productionOrder);
                productionOrderOperation.setOperation(actualProductOperationsOrder.get(pId).get(i));

                int lastWorker = -1;
                long partStart = 0;
                long partEnd = 0;
                List<EmployeeProductionOrderOperation> employeeProductionOrderOperations = new ArrayList<>();
                for (int j = 0; j < productsFinalMapping.get(pId).get(operation).size(); j++) {
                    AvailabilityMapping mapping = productsFinalMapping.get(pId).get(operation).get(j);
                    if (lastWorker == -1) {
                        if (mapping.occupied == 1) {
                            partStart = mapping.minutes;
                            partEnd = partStart + minimalTimeUnitMinutes;
                            lastWorker = mapping.worker;
                        }
                    } else {
                        if (mapping.occupied == 0) {
                            employeeProductionOrderOperations.add(getEmployeeProductionOrderOperation(startTime, productionOrderOperation, lastWorker, partStart, partEnd));
                            lastWorker = -1;
                        } else {
                            if (mapping.minutes == partEnd && lastWorker == mapping.worker) {
                                partEnd += minimalTimeUnitMinutes;
                            } else {
                                employeeProductionOrderOperations.add(getEmployeeProductionOrderOperation(startTime, productionOrderOperation, lastWorker, partStart, partEnd));
                                partStart = mapping.minutes;
                                partEnd = partStart + minimalTimeUnitMinutes;
                                lastWorker = mapping.worker;
                            }

                            if (j == productsFinalMapping.get(pId).get(operation).size() - 1) {
                                employeeProductionOrderOperations.add(getEmployeeProductionOrderOperation(startTime, productionOrderOperation, lastWorker, partStart, partEnd));
                            }
                        }
                    }
                }
                productionOrderOperation.setEmployeeProductionOrderOperations(employeeProductionOrderOperations);
            }
        }

        for (int i = 0; i < orders.size(); i++) {
            orders.set(i, orderService.insert(orders.get(i)));
        }

//        System.out.println("=================");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        System.out.println();
//        for (Order order : orders) {
//            System.out.println("Order for product: " + order.getProduct().getName());
//            for (ProductionOrderOperation productionOrderOperation : order.getProductionOrder().getProductionOrderOperations()) {
//                System.out.println("Machine " + productionOrderOperation.getOperation().getMachine().getId() +
//                        " [" + productionOrderOperation.getBeginn().format(formatter) + " - " +
//                        productionOrderOperation.getEnd().format(formatter) + "] ");
//                for (EmployeeProductionOrderOperation employeeProductionOrderOperation :
//                        productionOrderOperation.getEmployeeProductionOrderOperations()) {
//                    System.out.println("  [ W:" + employeeProductionOrderOperation.getEmployee().getId() +
//                            " => " + employeeProductionOrderOperation.getBeginn().format(formatter) +
//                            " - " + employeeProductionOrderOperation.getEnd() + " ]"
//                    );
//                }
//            }
//            System.out.println();
//        }
    }

    private EmployeeProductionOrderOperation getEmployeeProductionOrderOperation(LocalDateTime startTime, ProductionOrderOperation productionOrderOperation, int lastWorker, long partStart, long partEnd) {
        EmployeeProductionOrderOperation employeeProductionOrderOperation = new EmployeeProductionOrderOperation();
        employeeProductionOrderOperation.setEmployee(employees.get(lastWorker));
        employees.get(lastWorker).getEmployeeProductionOrderOperations().add(employeeProductionOrderOperation);
        employeeProductionOrderOperation.setProductionOrderOperation(productionOrderOperation);
        employeeProductionOrderOperation.setBeginn(startTime.plusMinutes(partStart));
        employeeProductionOrderOperation.setEnd(startTime.plusMinutes(partEnd));
        return employeeProductionOrderOperation;
    }

    private static String repeat(String str, int times) {
        if (times < 0) {
            times = 5;
        }
        return new String(new char[times]).replace("\0", str);
    }

    public static void main(String[] args) throws Exception {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("app-context.xml");
        ctx.refresh();

        EmployeeService employeeService = ctx.getBean("employeeService", EmployeeService.class);
        List<Employee> employees = employeeService.findAll();

        MachineService machineService = ctx.getBean("machineService", MachineService.class);
        List<Machine> allMachines = machineService.findAll();

        ProductService productService = ctx.getBean("productService", ProductService.class);
        List<Product> products = productService.findAll();

        OrderService orderService = ctx.getBean("orderService", OrderService.class);

        List<Order> orders = new ArrayList<>();
        for (Product product : products) {
            Order order = new Order();
            order.setDescription("Order for Product");
            order.setProduct(product);
            order.setAmount(1);
            product.getOrders().add(order);

            ProductionOrder productionOrder = new ProductionOrder();
            productionOrder.setOrder(order);
            order.setProductionOrder(productionOrder);
            productionOrder.setDateOfDelivery(new Date(2018-1900, 2, 1));
            productionOrder.setWorkschedule(product.getWorkschedule());
            product.getWorkschedule().getProductionOrders().add(productionOrder);

            orders.add(order);
        }

        int workingDayBeginTime = 8;
        int workingDayDuration = 12;
        int breakDurationMinutes = 30;
        int maxHoursWithoutBreak = 4;
        int maxAllowedHoursForWorker = 8;

        JobShop jobShop = new JobShop(
                workingDayBeginTime,
                workingDayDuration,
                breakDurationMinutes,
                maxHoursWithoutBreak,
                maxAllowedHoursForWorker,
                allMachines,
                employees,
                orders,
                orderService
        );
        orders = jobShop.run();

        List<Order> ordersNew = orderService.findAll();
        System.out.println("=================");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println();
        for (Order order : ordersNew) {
            System.out.println("Order for product: " + order.getProduct().getName());
            for (ProductionOrderOperation productionOrderOperation : order.getProductionOrder().getProductionOrderOperations()) {
                System.out.println("Machine " + productionOrderOperation.getOperation().getMachine().getId() +
                        " [" + productionOrderOperation.getBeginn().format(formatter) + " - " +
                        productionOrderOperation.getEnd().format(formatter) + "] ");
                for (EmployeeProductionOrderOperation employeeProductionOrderOperation :
                        productionOrderOperation.getEmployeeProductionOrderOperations()) {
                    System.out.println("  [ W:" + employeeProductionOrderOperation.getEmployee().getId() +
                            " => " + employeeProductionOrderOperation.getBeginn().format(formatter) +
                            " - " + employeeProductionOrderOperation.getEnd() + " ]"
                    );
                }
            }
            System.out.println();
        }
    }
}
