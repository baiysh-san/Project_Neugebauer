package pti.model.service;

import pti.model.domain.Machine;

import java.util.List;

public interface MachineService {
	Machine findById(Long id);
	List<Machine> findAll();
	Machine insert(Machine machine);
	Machine update(Machine machine);
	void delete(Machine machine);
    List<Machine> findByOperative(boolean operative);
}
