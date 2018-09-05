package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pti.model.domain.Order;
import pti.model.domain.Product;
import pti.model.repository.ProductRepository;

import java.util.List;

@Service("productService")
@Repository
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository pr;

	public Product findById(Long id) {
		return pr.findOne(id);
	}
	
	public Product findByName(String name) {
		return pr.findByName(name);
	}
	
	public Product findByOrder(Order order) {
		return pr.findByOrdersIn(order);
	}

	public List<Product> findAll() {
		return pr.findAll();
	}
	
	public void delete(Product product){
		pr.delete(product);
	}

	@Override
	public Product insert(Product product) {
		return pr.saveAndFlush(product);
	}

	@Override
	public Product update(Product product) {
		return pr.saveAndFlush(product);
		
	}

	
}
