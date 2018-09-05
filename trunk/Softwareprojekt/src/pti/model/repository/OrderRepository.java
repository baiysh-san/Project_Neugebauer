package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pti.model.domain.Customer;
import pti.model.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}
