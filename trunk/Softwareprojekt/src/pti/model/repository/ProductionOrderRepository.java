package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.domain.ProductionOrder;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    ProductionOrder findByWorkschedule_Product(Product product);
    ProductionOrder findByOrder(Order order);
}
