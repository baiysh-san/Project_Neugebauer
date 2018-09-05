package pti.model.service;

import pti.model.domain.Order;
import pti.model.domain.Product;

import java.util.List;

public interface ProductService {
	Product findById(Long id);
	Product findByName(String name);
	Product findByOrder(Order order);
	List<Product> findAll();
	void delete(Product product);
	Product insert(Product product);
	Product update(Product product);
}
