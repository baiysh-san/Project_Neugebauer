package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pti.model.domain.Machine;
import pti.model.repository.MachineRepository;

import java.util.List;

@Service("machineService")
@Repository
public class MachineServiceImpl implements MachineService {
	@Autowired
	private MachineRepository mr;

	public Machine findById(Long id) {
		return mr.findOne(id);
	}
	public List<Machine> findAll() {
		return mr.findAll();
	}
	
	public Machine insert(Machine machine) {
		return mr.saveAndFlush(machine);
	}
	
	public Machine update(Machine machine) {
		return mr.saveAndFlush(machine);
	}

	public void delete(Machine machine) {
		mr.delete(machine);
	}

    public List<Machine> findByOperative(boolean operative) {
        return mr.findByOperative(operative);
    }
}
