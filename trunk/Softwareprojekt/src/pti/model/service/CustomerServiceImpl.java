package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pti.model.domain.Customer;
import pti.model.domain.Order;
import pti.model.repository.CustomerRepository;

import java.util.List;

@Service("customerService")
@Repository
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository cr;

	public Customer findById(Long id) {
		return cr.findOne(id);
	}
	
	public Customer findByFirstName(String firstName) {
		return cr.findByFirstName(firstName);
	}
	
	public Customer findByOrder(Order order) {
		return cr.findByOrdersIs(order);
	}

	public List<Customer> findAll() {
		return cr.findAll();
	}

	public Customer insert(Customer customer) {
		return cr.saveAndFlush(customer);
	}

	public Customer update(Customer customer) {
		return cr.saveAndFlush(customer);
	}

	public void delete(Customer customer) {
		cr.delete(customer);
	}

}
