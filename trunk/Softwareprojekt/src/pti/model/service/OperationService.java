package pti.model.service;

import pti.model.domain.Operation;
import pti.model.domain.Product;

import java.util.List;

public interface OperationService {
	Operation findById(Long id);
	List<Operation> findByProduct(Product product);
	List<Operation> findAll();
	Operation insert(Operation operation);
	Operation update(Operation operation);
	void delete(Operation operation);
}
