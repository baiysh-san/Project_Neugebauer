package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pti.model.domain.Employee;
import pti.model.repository.EmployeeRepository;

import java.util.List;

@Service("employeeService")
@Repository
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository er;

	public Employee findById(Long id) {
		return er.findOne(id);
	}

	public List<Employee> findAll() {
		return er.findAll();
	}

	public Employee insert(Employee employee) {
		return er.saveAndFlush(employee);
	}

	public Employee update(Employee employee) {
		return er.saveAndFlush(employee);
	}

	public void delete(Employee employee) {
		er.delete(employee);
	}

    public List<Employee> findByAvailable(boolean available) {
        return er.findByAvailable(available);
    }

}
