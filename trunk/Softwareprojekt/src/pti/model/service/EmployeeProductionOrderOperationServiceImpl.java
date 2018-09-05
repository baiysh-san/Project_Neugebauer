package pti.model.service;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pti.model.domain.EmployeeProductionOrderOperation;

@Service("em")
@Repository
public class EmployeeProductionOrderOperationServiceImpl implements EmployeeProductionOrderOperationService {

    public EmployeeProductionOrderOperation findById(Long id) {
        return null;
    }
}
