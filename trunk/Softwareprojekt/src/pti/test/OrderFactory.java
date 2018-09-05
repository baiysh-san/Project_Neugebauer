package pti.test;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;

import org.springframework.context.support.GenericXmlApplicationContext;

import pti.model.domain.*;
import pti.model.service.EmployeeService;
import pti.model.service.OrderService;

/**
 * class that creates orders.
 * 
 * @author modell team
 */
public class OrderFactory {

	private static LocalDateTime beginnWorking = LocalDateTime.of(2018, Month.MAY, 28, 7, 0, 0, 0);
	private static LocalDateTime endWorking = LocalDateTime.of(2018, Month.MAY, 28, 19, 0, 0, 0);
	private static LocalDateTime currentTime = LocalDateTime.of(2018, Month.MAY, 28, 7, 0, 0, 0);

	public static Order createOrder(String description, Date dateOfDelivery, Product product, int amount, GenericXmlApplicationContext ctx) {
		if (amount <= 0)
			return null; // negative value isn't allowed for amount
		// Creating an order
		EmployeeService es = ctx.getBean("employeeService", EmployeeService.class);
		Employee saegeEmployee = es.findById(1L);
		Employee hobelEmployee = es.findById(2L);
		Employee bohreEmployee = es.findById(3L);
		Employee politurEmployee = es.findById(4L);
		Employee verpackeEmployee = es.findById(5L);

		Order order = new Order();
		order.setDescription(description);
		order.setProduct(product);
		order.setAmount(amount);
		product.getOrders().add(order);

		// Creating of a productionOrder
		ProductionOrder productionOrder = new ProductionOrder();
		productionOrder.setOrder(order);
		order.setProductionOrder(productionOrder);
		productionOrder.setDateOfDelivery(dateOfDelivery);
		productionOrder.setWorkschedule(product.getWorkschedule());
		product.getWorkschedule().getProductionOrders().add(productionOrder);
        EmployeeProductionOrderOperation employeeProductionOrderOperation = new EmployeeProductionOrderOperation();

		// Creating of the ProductionOrderOperations for each operation
		// with a specified amount of times
		for (int i = 0; i < amount; i++) {
			ProductionOrderOperation prodOrdOperSaegen = new ProductionOrderOperation();
			prodOrdOperSaegen = moveCurrentTime(product, prodOrdOperSaegen, 0);
			productionOrder.getProductionOrderOperations().add(prodOrdOperSaegen);
			prodOrdOperSaegen.setProductionOrder(productionOrder);
			prodOrdOperSaegen.setOperation(product.getWorkschedule().getOperations().get(0));
	
			EmployeeProductionOrderOperation ep = new EmployeeProductionOrderOperation();
			ep.setEmployee(saegeEmployee);
			saegeEmployee.getEmployeeProductionOrderOperations().add(ep);
			prodOrdOperSaegen.getEmployeeProductionOrderOperations().add(ep);
			ep.setProductionOrderOperation(prodOrdOperSaegen);
			ep.setDuration(product.getWorkschedule().getOperations().get(0).getDuration());
			ep.setBeginn(prodOrdOperSaegen.getBeginn());
		}
		for (int i = 0; i < amount; i++) {
			ProductionOrderOperation prodOrdOperHobeln = new ProductionOrderOperation();
			prodOrdOperHobeln = moveCurrentTime(product, prodOrdOperHobeln, 1);
			productionOrder.getProductionOrderOperations().add(prodOrdOperHobeln);
			prodOrdOperHobeln.setProductionOrder(productionOrder);
			prodOrdOperHobeln.setOperation(product.getWorkschedule().getOperations().get(1));
			

			EmployeeProductionOrderOperation ep = new EmployeeProductionOrderOperation();
			ep.setEmployee(hobelEmployee);
			hobelEmployee.getEmployeeProductionOrderOperations().add(ep);
			prodOrdOperHobeln.getEmployeeProductionOrderOperations().add(ep);
			ep.setProductionOrderOperation(prodOrdOperHobeln);
			ep.setDuration(product.getWorkschedule().getOperations().get(1).getDuration());
			ep.setBeginn(prodOrdOperHobeln.getBeginn());
		}

		for (int i = 0; i < amount; i++) {
			ProductionOrderOperation prodOrdOperBohren = new ProductionOrderOperation();
			prodOrdOperBohren = moveCurrentTime(product, prodOrdOperBohren, 2);
			productionOrder.getProductionOrderOperations().add(prodOrdOperBohren);
			prodOrdOperBohren.setProductionOrder(productionOrder);
			prodOrdOperBohren.setOperation(product.getWorkschedule().getOperations().get(2));
			
			EmployeeProductionOrderOperation ep = new EmployeeProductionOrderOperation();
			ep.setEmployee(bohreEmployee);
			bohreEmployee.getEmployeeProductionOrderOperations().add(ep);
			prodOrdOperBohren.getEmployeeProductionOrderOperations().add(ep);
			ep.setProductionOrderOperation(prodOrdOperBohren);
			ep.setDuration(product.getWorkschedule().getOperations().get(2).getDuration());
			ep.setBeginn(prodOrdOperBohren.getBeginn());
			
//			bohreEmployee.getProductionOrderOperations().add(prodOrdOperBohren);
//			prodOrdOperBohren.getEmployees().add(bohreEmployee);
		}

		for (int i = 0; i < amount; i++) {
			ProductionOrderOperation prodOrdOperPolitur = new ProductionOrderOperation();
			prodOrdOperPolitur = moveCurrentTime(product, prodOrdOperPolitur, 3);
			productionOrder.getProductionOrderOperations().add(prodOrdOperPolitur);
			prodOrdOperPolitur.setProductionOrder(productionOrder);
			prodOrdOperPolitur.setOperation(product.getWorkschedule().getOperations().get(3));
			
			EmployeeProductionOrderOperation ep = new EmployeeProductionOrderOperation();
			ep.setEmployee(politurEmployee);
			politurEmployee.getEmployeeProductionOrderOperations().add(ep);
			prodOrdOperPolitur.getEmployeeProductionOrderOperations().add(ep);
			ep.setProductionOrderOperation(prodOrdOperPolitur);
			ep.setDuration(product.getWorkschedule().getOperations().get(3).getDuration());
			ep.setBeginn(prodOrdOperPolitur.getBeginn());
			
//			politurEmployee.getProductionOrderOperations().add(prodOrdOperPolitur);
//			prodOrdOperPolitur.getEmployees().add(politurEmployee);
		}

		for (int i = 0; i < amount; i++) {
			ProductionOrderOperation prodOrdOperVerpacken = new ProductionOrderOperation();
			moveCurrentTime(product, prodOrdOperVerpacken, 4);
			productionOrder.getProductionOrderOperations().add(prodOrdOperVerpacken);
			prodOrdOperVerpacken.setProductionOrder(productionOrder);
			prodOrdOperVerpacken.setOperation(product.getWorkschedule().getOperations().get(4));
			EmployeeProductionOrderOperation ep = new EmployeeProductionOrderOperation();
			ep.setEmployee(verpackeEmployee);
			verpackeEmployee.getEmployeeProductionOrderOperations().add(ep);
			prodOrdOperVerpacken.getEmployeeProductionOrderOperations().add(ep);
			ep.setProductionOrderOperation(prodOrdOperVerpacken);
			ep.setDuration(product.getWorkschedule().getOperations().get(4).getDuration());
			ep.setBeginn(prodOrdOperVerpacken.getBeginn());
			
//			verpackeEmployee.getProductionOrderOperations().add(prodOrdOperVerpacken);
//			prodOrdOperVerpacken.getEmployees().add(verpackeEmployee);
		}
		OrderService orderService = ctx.getBean("orderService", OrderService.class);
		order = orderService.insert(order);
		return order;
	}

	/**
	 * sets out start and end time for a ProductionOrderOperation returns changed
	 * ProductionOrderOperation
	 * 
	 * @param product
	 *            - current Product
	 * @param proOrdOper
	 *            - current ProductionOrderOperation
	 * @param operationOrder
	 *            - order number of Operation (see test-data.sql)
	 * @return
	 */
	private static ProductionOrderOperation moveCurrentTime(Product product, ProductionOrderOperation proOrdOper,
			int operationOrder) {
		// the next working day, if the working time has expired
		if (currentTime.isAfter(endWorking)) {
			beginnWorking = beginnWorking.plusHours(24);
			endWorking = endWorking.plusHours(24);
			currentTime = beginnWorking;
		}

		LocalDateTime beginTime = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
				currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute());
		proOrdOper.setBeginn(beginTime);
		currentTime = currentTime
				.plusMinutes(product.getWorkschedule().getOperations().get(operationOrder).getDuration());
		LocalDateTime endTime = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
				currentTime.getDayOfMonth(), currentTime.getHour(), currentTime.getMinute());
		proOrdOper.setEnd(endTime);
		currentTime.plusMinutes(1);

		return proOrdOper;
	}
}
