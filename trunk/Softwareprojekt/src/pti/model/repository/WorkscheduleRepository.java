package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.model.domain.Product;
import pti.model.domain.Workschedule;

public interface WorkscheduleRepository extends JpaRepository<Workschedule, Long> {
    Workschedule findByProduct(Product product);
}
