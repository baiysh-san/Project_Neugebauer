package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.model.domain.Order;
import pti.model.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    Product findByOrdersIn(Order order);
}
