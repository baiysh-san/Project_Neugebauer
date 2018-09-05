package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pti.model.domain.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    List<Employee> findByAvailable(boolean available);
}
