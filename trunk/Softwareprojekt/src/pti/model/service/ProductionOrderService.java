package pti.model.service;

import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.domain.ProductionOrder;

import java.util.List;

public interface ProductionOrderService {
	ProductionOrder findByProduct(Product product);
	ProductionOrder findById(Long id);
	List<ProductionOrder> findAll();
	ProductionOrder findByOrder(Order order);
	ProductionOrder insert(ProductionOrder order);
	ProductionOrder update(ProductionOrder order);
	void delete(ProductionOrder order);
}
