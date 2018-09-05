package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pti.model.domain.Operation;
import pti.model.domain.Product;
import pti.model.repository.OperationRepository;

import java.util.List;

@Service("operationService")
@Repository
public class OperationServiceImpl implements OperationService {
	@Autowired
	private OperationRepository or;

	public Operation findById(Long id) {
		return or.findOne(id);
	}

	public List<Operation> findByProduct(Product product) {
		return or.findByWorkschedule_Product(product);
	}

	public List<Operation> findAll() {
		return or.findAll();
	}

	@Override
	public Operation insert(Operation operation) {
		return or.saveAndFlush(operation);
	}

	@Override
	public Operation update(Operation operation) {
		return or.saveAndFlush(operation);
	}

	@Override
	public void delete(Operation operation) {
		or.delete(operation);
	}
}
