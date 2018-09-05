package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.model.domain.EmployeeProductionOrderOperation;

public interface EmployeeProductionOrderOperationRepository extends JpaRepository<EmployeeProductionOrderOperation, Long> {
}
