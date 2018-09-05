package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pti.model.domain.Machine;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long>{
    List<Machine> findByOperative(boolean operative);
}
