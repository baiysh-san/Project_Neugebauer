package pti.model.service;

import java.util.List;

import pti.model.domain.Customer;
import pti.model.domain.Order;

public interface OrderService {
	Order findById(Long id);

	List<Order> findByCustomer(Customer customer);

	List<Order> findAll();
	
	Order insert(Order order);
	
	Order update(Order order);
	
	void delete(Order order);
	
	
}
