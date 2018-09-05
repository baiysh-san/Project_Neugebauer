package pti.model.service;

import pti.model.domain.Employee;

import java.util.List;

public interface EmployeeService {
	Employee findById(Long id);
	List<Employee> findAll();
	Employee insert(Employee employee);
	Employee update(Employee employee);
	void delete(Employee employee);
	List<Employee> findByAvailable(boolean available);
}
