package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pti.model.domain.Customer;
import pti.model.domain.Order;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByOrdersIs(Order order);
    Customer findByFirstName(String firstName);
}
