package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.domain.ProductionOrder;
import pti.model.repository.ProductionOrderRepository;

import java.util.List;

@Service("productionOrderService")
@Repository
public class ProductionOrderServiceImp implements ProductionOrderService {
	@Autowired
	ProductionOrderRepository pOr;

	public ProductionOrder findByProduct(Product product) {
		return pOr.findByWorkschedule_Product(product);
	}

	public ProductionOrder findById(Long id) {
		return pOr.findOne(id);
	}

	public List<ProductionOrder> findAll() {
		return pOr.findAll();
	}

	public ProductionOrder findByOrder(Order order) {
		return pOr.findByOrder(order);
		
	}

	@Override
	public ProductionOrder insert(ProductionOrder order) {
		return pOr.saveAndFlush(order);
	}

	@Override
	public ProductionOrder update(ProductionOrder order) {
		return	pOr.saveAndFlush(order);
	}

	@Override
	public void delete(ProductionOrder order) {
		pOr.delete(order);
	}
}
