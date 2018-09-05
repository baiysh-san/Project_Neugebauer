package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pti.model.domain.Operation;
import pti.model.domain.Product;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByWorkschedule_Product(Product product);
}
