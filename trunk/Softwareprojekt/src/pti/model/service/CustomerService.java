package pti.model.service;

import pti.model.domain.Customer;
import pti.model.domain.Order;

import java.util.List;

public interface CustomerService {
	Customer findById(Long id);
	Customer findByFirstName(String firstName);
	Customer findByOrder(Order order);
	List<Customer> findAll();
	Customer insert(Customer customer);
    Customer update(Customer customer);
    void delete(Customer customer);
}
