package pti.test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.google.common.collect.Lists;

import pti.model.domain.*;
import pti.model.service.AdminService;
import pti.model.service.CustomerService;
import pti.model.service.EmployeeService;
import pti.model.service.MachineService;
import pti.model.service.OperationService;
import pti.model.service.OrderService;
import pti.model.service.ProductService;
import pti.model.service.ProductionOrderService;
import pti.model.service.WorkscheduleService;

public class Main {

	static GenericXmlApplicationContext ctx;
	
	public static void main(String args[]) {
		ctx = new GenericXmlApplicationContext();
	
		ctx.load("app-context.xml");
		ctx.refresh();
//		insertCustomer(); //this method must be always called, because another test methods are depend on it!!!!
//		customerByOrder();
//		orderByCustomer();
//		operationByProduct();
//		productionOrderByProduct();
//		workscheduleByProduct();
//	    productByOrder();
//		productFindByName();
//		findAllDemo();
//		productionOrderByOrder();
//	    findAllDemo();
//	    employeeByAvailable();
//      machineByAvailable();
//      createOrderWithExistingCustomerAndProduct();
//      updateCustomer();
//      testCreatingOfTheOrders();
//      deleteCustomer();
//      insertAdmin();
//    	insertProduct();
//		testCreatingOfTheOrders();
		deletingOrders();
	}
	
	private static void deletingOrders(){
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(1L);
		Customer customer2 = cs.findById(2L);
		
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product tisch = ps.findByName("Tisch");
		Product stuhl = ps.findByName("Stuhl");
		Product schrank = ps.findByName("Schrank");
		
		//Receiving an order for a given product with a specified quantity 
		//with a productionOrder and the ProductionOrderOperations
		int amountOfProduct = 1;
		Order orderOfTish = OrderFactory.createOrder("Order of Tisch", new Date(2018-1900, 2, 1),tisch, 
												amountOfProduct, ctx);
		Order orderOfStuhl = OrderFactory.createOrder("Order of Stuhl", new Date(2018-1900, 2, 1), stuhl, 
												amountOfProduct, ctx);
		Order orderOfSschrank = OrderFactory.createOrder("Order of Scrank", new Date(2018-1900, 2, 1), schrank, 
				amountOfProduct, ctx);
		
		customer.getOrders().add(orderOfTish);
		orderOfTish.setCustomer(customer);
		customer.getOrders().add(orderOfStuhl);
		orderOfStuhl.setCustomer(customer);
		
		customer2.getOrders().add(orderOfSschrank);
		orderOfSschrank.setCustomer(customer2);
		
		customer = cs.update(customer);
		customer2 = cs.update(customer2);
		
		OrderService os = ctx.getBean("orderService", OrderService.class);
		System.err.println("Before deleting an order (3 orders must exist):");
		List<Order> orders = os.findAll();
		orders.forEach(o -> printOutOrderDetails(o));
		
		os.delete(orderOfSschrank);
		
		System.err.println("After deleting an order  (2 orders must exist) :");
		orders = os.findAll();
		orders.forEach(o -> printOutOrderDetails(o));
		System.out.println("Customer of deleted order (must be not null :)"+ cs.findById(2L));
	}
	
	
	private static void testCreatingOfTheOrders() {
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(1L);
		Customer customer2 = cs.findById(2L);
		
		//extract some products for which you want to create an order (Tisch, Stuhl)
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product tisch = ps.findByName("Tisch");
		Product stuhl = ps.findByName("Stuhl");
		Product schrank = ps.findByName("Schrank");
		
		//Receiving an order for a given product with a specified quantity 
		//with a productionOrder and the ProductionOrderOperations
		int amountOfProduct = 1;
		Order orderOfTish = OrderFactory.createOrder("Order of Tisch", new Date(2018-1900, 2, 1),tisch, 
												amountOfProduct, ctx);
		Order orderOfStuhl = OrderFactory.createOrder("Order of Stuhl", new Date(2018-1900, 2, 1), stuhl, 
												amountOfProduct, ctx);
		
		Order orderOfSschrank = OrderFactory.createOrder("Order of Scrank", new Date(2018-1900, 2, 1), schrank, 
				amountOfProduct, ctx);

		System.out.println();
		
		//Setting relationships customer<-->order
		customer.getOrders().add(orderOfTish);
		orderOfTish.setCustomer(customer);
		customer.getOrders().add(orderOfStuhl);
		orderOfStuhl.setCustomer(customer);
		
		customer2.getOrders().add(orderOfSschrank);
		orderOfSschrank.setCustomer(customer2);
		
		customer = cs.update(customer);
		customer2 = cs.update(customer2);
		
		System.out.println();
		OrderService os = ctx.getBean("orderService", OrderService.class);
		//fetching  all orders
		List<Order> orders = os.findAll();
		orders.forEach(o -> printOutOrderDetails(o));
		
	}
	
	private static void printOutOrderDetails(Order order){
		List<ProductionOrderOperation> prodOrderOperations = order.getProductionOrder().getProductionOrderOperations();
		if(order.getCustomer() != null){
			System.err.println("Order of cutomer : "+order.getCustomer().getLastName());
		}	
		System.out.println("Name  of product : "+order.getProduct().getName());
		System.out.println("Amount  of product : "+order.getAmount());
		System.out.println("Description of order : "+order.getDescription());
		
		System.out.println("Employee  <--->  Operations:");
		for(ProductionOrderOperation poo : prodOrderOperations){
				System.out.println(
						"	Employee "+poo.getEmployeeProductionOrderOperations().get(0).getEmployee().getLastName()+
						" works at operation "+
						poo.getOperation().getName()+" within "
						+poo.getEmployeeProductionOrderOperations().get(0).getDuration()
						+" minutes"
						);
			
		}
	}
	
	private static void updateCustomer() {
		System.out.println();
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(1L);
		System.out.println("Firstname before update "+customer.getFirstName());
		customer.setFirstName("Test name");
		System.out.println("Firstname after update "+cs.update(customer).getFirstName());
	}

	private static void deleteCustomer() {
		System.out.println();
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(1L);
		cs.delete(customer);
		customer = cs.findById(1L);
		System.out.println("customer must be null -> "+customer);
	}



	private static void createOrderWithExistingCustomerAndProduct() {
		System.out.println();
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findByFirstName("Lionel");
	
		Order order = new Order();
		order.setDescription("An order of Lionel Messi");
		
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findByName("Tisch");
		
		order.setProduct(product);      // relationship order - product
		product.getOrders().add(order);
		order.setCustomer(customer);    // relationship order - product
		customer.getOrders().add(order);
		
		cs.update(customer);
		
		customer = cs.findById(1l);
		
		System.out.println(customer.getOrders());

	}



	private static void insertCustomer() {
		//Creating objects. Without defining relationships
		Customer customer = new Customer();
		customer.setFirstName("Roy");
		customer.setLastName("Kane");
		customer.setAddress("Germany");
		
		Order order = new Order();
		order.setDescription("An order of Mr. Kane");
		order.setAmount(2);
		
		Product product = new Product();
		product.setName("Chair");
		product.setDescription("description of chair");
		
		
		Workschedule workschedule = new Workschedule();
		workschedule.setDescription("A Workschedule of Chair");
		
		Operation operation = new Operation();
		operation.setName("name");
		operation.setDuration(120);
		Operation operation2 = new Operation();
		operation2.setName("asdadads");
		operation2.setDuration(220);
		
		Machine machine = new Machine();
		machine.setName("Chair Machine");
		machine.setDescription("Machine of Chair");
		
		ProductionOrder productionOrder = new ProductionOrder();
		productionOrder.setDateOfDelivery(new Date(2019, 5, 5));
		
		Employee employee = new Employee();
		employee.setFirstName("Asan");
		employee.setLastName("Usenov");
		employee.setAddress("Kyrgyzstan");
		
		Employee employee2 = new Employee();
		employee2.setFirstName("Aygul");
		employee2.setLastName("Usenova");
		employee2.setAddress("Kyrgyzstan");
		
		
		ProductionOrderOperation productionOrderOperation = new ProductionOrderOperation();
		productionOrderOperation.setBeginn(LocalDateTime.now());
		productionOrderOperation.setEnd(LocalDateTime.now());
		
		ProductionOrderOperation productionOrderOperation2 = new ProductionOrderOperation();
		productionOrderOperation2.setBeginn(LocalDateTime.now());
		productionOrderOperation2.setEnd(LocalDateTime.now());
		Set<ProductionOrderOperation> productionOrderOperations = new HashSet<>();
		productionOrderOperations.add(productionOrderOperation);
		Set<ProductionOrderOperation> productionOrderOperations2 = new HashSet<>();
		productionOrderOperations2.add(productionOrderOperation2);

        EmployeeProductionOrderOperation employeeProductionOrderOperation =
                new EmployeeProductionOrderOperation();
        employeeProductionOrderOperation.setEmployee(employee);
        employeeProductionOrderOperation.setProductionOrderOperation(productionOrderOperation);
        employeeProductionOrderOperation.setBefore(null);
        employeeProductionOrderOperation.setAfter(null);
        employeeProductionOrderOperation.setDuration(30);

        EmployeeProductionOrderOperation employeeProductionOrderOperation2 =
                new EmployeeProductionOrderOperation();
        employeeProductionOrderOperation2.setEmployee(employee2);
        employeeProductionOrderOperation2.setProductionOrderOperation(productionOrderOperation2);
        employeeProductionOrderOperation2.setDuration(25);

		
		//Definition of relationships of created objects
		
		product.setWorkschedule(workschedule); //product - workschedule
		workschedule.setProduct(product);
		
		workschedule.getOperations().add(operation); //workschedule - operations
		workschedule.getOperations().add(operation2);
		operation.setWorkschedule(workschedule);
		operation2.setWorkschedule(workschedule);
		
		operation.setBeforeOperation(null);
		operation.setAfterOperation(operation2);
		operation2.setAfterOperation(null);
		operation2.setBeforeOperation(operation);
		operation.setMachine(machine);             //operation - maschine
		machine.getOperations().add(operation);
		operation2.setMachine(machine);
		machine.getOperations().add(operation2);
		

		customer.getOrders().add(order);  // customer - order
		order.setCustomer(customer);
		
		product.getOrders().add(order);   //product - order
		order.setProduct(product);        
		
		workschedule.getProductionOrders().add(productionOrder); // workschedule - productionOrder		
		productionOrder.setWorkschedule(workschedule);
		
		productionOrder.setOrder(order);   // productionOrder - order
		order.setProductionOrder(productionOrder);
		
		productionOrder.getProductionOrderOperations().add(productionOrderOperation); //productionOrder - productionOrderOperation 
		productionOrderOperation.setProductionOrder(productionOrder);
		productionOrder.getProductionOrderOperations().add(productionOrderOperation2);
		productionOrderOperation2.setProductionOrder(productionOrder);
		
//		productionOrderOperation.getEmployees().add(employee); //productionOrderOperation - employee
//		employee.getProductionOrderOperations().add(productionOrderOperation);
//
//		productionOrderOperation2.getEmployees().add(employee2);
//		employee2.getProductionOrderOperations().add(productionOrderOperation2);
		
		operation.getProductionOrderOperations().add(productionOrderOperation); // productionOrderOperation - operation;
		productionOrderOperation.setOperation(operation);
		
		operation2.getProductionOrderOperations().add(productionOrderOperation2);
		productionOrderOperation2.setOperation(operation2);
		
		operation.setMachine(machine);
		machine.getOperations().add(operation);
		
		operation2.setMachine(machine);
		machine.getOperations().add(operation2);
		//Saving in the database
		
		
		
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		
		Customer  insertedCustomer = cs.insert(customer); 
		
		//Testing
		
		System.out.println(cs.findById(insertedCustomer.getId()));//cheking of according to the database
		System.out.println();
		System.err.println("customer:");
		System.out.println(insertedCustomer);
		
		List<Order> insertedOrders = Lists.newArrayList(insertedCustomer.getOrders());
		System.err.println("orders of customer:");
		System.out.println(insertedOrders);
		
		System.err.println("first order of customer:");
		Order insertedOrder = insertedOrders.get(0);
		System.out.println(insertedOrder);
		
		
		Product insertedProduct = insertedOrder.getProduct();
		System.err.println("first product of first order:");
		System.out.println(insertedProduct);
		
		Workschedule insertedWorkschedule = insertedProduct.getWorkschedule();
		System.err.println("first workschedule of first product:");
		System.out.println(insertedWorkschedule);
		
		List<Operation> instertedOperations = Lists.newArrayList(workschedule.getOperations());
		System.err.println("operations of workschedule");
		System.out.println(instertedOperations);
		
		ProductionOrder insertedProductionOrder = Lists.newArrayList(insertedWorkschedule.getProductionOrders()).get(0);
		List<ProductionOrderOperation> insertedProductionOrderOperations 
				= Lists.newArrayList(insertedProductionOrder.getProductionOrderOperations());
		System.out.println(insertedProductionOrderOperations);
		System.out.println(insertedProductionOrderOperations.get(0).getOperation());
		

//		List<Employee> insertedEmployees = Lists.newArrayList(insertedProductionOrderOperations.get(0).getEmployees());
//		System.err.println("employees of first ProductionOrderOperations:");
//		System.out.println(insertedEmployees);
//
//		System.err.println("machine of employee");
//		System.out.println(instertedOperations.get(0).getMachine());
//
	}

	public static void orderByCustomer() {
		System.out.println();
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		Customer customer = cs.findById(5L);
		System.out.println(customer);
		
		OrderService os = ctx.getBean("orderService", OrderService.class);
		Order order = os.findByCustomer(customer).get(0);
		System.out.println(order);

	}

	public static void operationByProduct() {
		System.out.println();
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findById(1l);
		
		OperationService os = ctx.getBean("operationService", OperationService.class);
		Operation operation = os.findByProduct(product).get(0);
		System.out.println(operation);
	}

	public static void productionOrderByProduct() {
		System.out.println();
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findById(8l);
		
		ProductionOrderService pos = ctx.getBean("productionOrderService", ProductionOrderService.class);
		ProductionOrder po = pos.findByProduct(product);
		System.out.println(po);

	}

	public static void workscheduleByProduct() {
		System.out.println();
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findById(8l);
		
		WorkscheduleService ws = ctx.getBean("workscheduleService", WorkscheduleService.class);
		Workschedule workschedule = ws.findByProduct(product);
		System.out.println(workschedule);
	}

	public static void productByOrder() {
		System.out.println();
		OrderService os = ctx.getBean("orderService", OrderService.class);
		Order order = os.findById(1l);
		
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findByOrder(order);
		System.out.println(product);

	}

	private static void productFindByName() {
		System.out.println();
		ProductService ps = ctx.getBean("productService", ProductService.class);
		Product product = ps.findByName("Tisch");
		System.out.println("Tisch ->" +product);
	}
	
	public static void findAllDemo() {
		System.out.println();
		CustomerService cs = ctx.getBean("customerService", CustomerService.class);
		List<Customer> customers = cs.findAll();
		System.out.println(customers);
	
		EmployeeService es = ctx.getBean("employeeService", EmployeeService.class);
		List<Employee> employees = es.findAll();
		System.out.println(employees);
		
		MachineService ms = ctx.getBean("machineService", MachineService.class);
		List<Machine> machines = ms.findAll();
		System.out.println(machines);
		
		OperationService os = ctx.getBean("operationService", OperationService.class);
		List<Operation> operations = os.findAll();
		System.out.println(operations);
		
		OrderService orderService = ctx.getBean("orderService", OrderService.class);
		List<Order> orders = orderService.findAll();
		System.out.println(orders);
		
		ProductionOrderService pos = ctx.getBean("productionOrderService", ProductionOrderService.class);
		List<ProductionOrder> productionOrders = pos.findAll();
		System.out.println(productionOrders);
		
		ProductService ps = ctx.getBean("productService", ProductService.class);
		List<Product> products = ps.findAll();
		System.out.println(products);
		
		WorkscheduleService ws = ctx.getBean("workscheduleService", WorkscheduleService.class);
		List<Workschedule> workschedules = ws.findAll();
		System.out.println(workschedules);
	}

	private static void customerByOrder(){
		System.out.println();
		OrderService orderService = ctx.getBean("orderService", OrderService.class);
		Order order = orderService.findById(1l);
		CustomerService cs = ctx.getBean(CustomerService.class);
		Customer customer = cs.findByOrder(order);
		System.out.println(customer);
	}
	private static void productionOrderByOrder() {
		System.out.println();
		OrderService orderService = ctx.getBean("orderService", OrderService.class);
		Order order = orderService.findById(1l);
		ProductionOrderService pos = ctx.getBean("productionOrderService", ProductionOrderService.class);
		ProductionOrder productionOrder = pos.findByOrder(order);
		System.out.println(productionOrder);
	}
	private static void employeeByAvailable() {
		System.out.println();
		EmployeeService es  = ctx.getBean("employeeService", EmployeeService.class);
	    List<Employee> employees = es.findByAvailable(false);
        System.out.println(employees);
    }
    private static void machineByAvailable() {
    	System.out.println();
    	MachineService ms = ctx.getBean("machineService", MachineService.class);
	    List<Machine> machines = ms.findByOperative(false);
        System.out.println(machines);
    }
    private static void insertProduct(){
    	Product product = new Product();
		product.setName("nameField.getText()");
		product.setDescription("descriptionField.getText()");

		Workschedule workschedule = new Workschedule();
		workschedule.setProduct(product);
		product.setWorkschedule(workschedule);

		MachineService ms = ctx.getBean("machineService", MachineService.class);
		System.out.println(ms.findAll());
		
		Operation o1 = new Operation();
		o1.setName("Sagen");
		o1.setDuration(22);
		Machine m = ms.findById(1L);
		o1.setMachine(m);
		m.getOperations().add(o1);
	
		workschedule.getOperations().add(o1);
		o1.setWorkschedule(workschedule);
		ProductService ps = ctx.getBean("productService", ProductService.class);
		
		product = ps.insert(product);
		product = ps.findById(product.getId());
		product.getWorkschedule().getOperations().forEach(o -> {
			System.out.println(o);
			System.out.println(o.getMachine().getName());
		});
    }
    
 
    
    private static void insertAdmin(){
    	AdminService as = ctx.getBean("adminService", AdminService.class);
    	Admin admin = new Admin();
    	admin.setLogin("login");
    	admin.setPassword("password");
    	as.insert(admin);
    	
    	Admin insertedAdmin = as.findByLogin("login");
    	System.out.println(insertedAdmin);
    	
    	
    }
}
