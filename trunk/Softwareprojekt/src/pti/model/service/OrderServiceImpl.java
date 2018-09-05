package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pti.model.domain.Customer;
import pti.model.domain.Order;
import pti.model.repository.OrderRepository;

import java.util.List;

@Service("orderService")
@Repository
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository or;

	public Order findById(Long id) {
		return or.findOne(id);
	}

	public List<Order> findByCustomer(Customer customer) {
		return or.findByCustomer(customer);
	}

	public List<Order> findAll() {
		return or.findAll();
	}

	public Order insert(Order order) {
		return or.saveAndFlush(order);
	}

	
	public Order update(Order order) {
		return or.saveAndFlush(order);
	}

	@Override
	public void delete(Order order) {
		or.delete(order);
	}
	
	
}
