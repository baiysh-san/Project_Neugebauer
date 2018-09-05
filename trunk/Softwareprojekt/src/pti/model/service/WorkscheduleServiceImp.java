package pti.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pti.model.domain.Product;
import pti.model.domain.Workschedule;
import pti.model.repository.WorkscheduleRepository;

import java.util.List;

@Service("workscheduleService")
@Repository
public class WorkscheduleServiceImp implements WorkscheduleService {
	@Autowired
	WorkscheduleRepository wr;

	public Workschedule findById(Long id) {
		return wr.findOne(id);
	}

	public Workschedule findByProduct(Product product) {
		return wr.findByProduct(product);
	}

	public List<Workschedule> findAll() {
		return wr.findAll();
	}

	public void delete(Workschedule workschedule) {
		wr.delete(workschedule);
	}

	@Override
	public Workschedule insert(Workschedule workschedule) {
		return wr.saveAndFlush(workschedule);
	}

	@Override
	public Workschedule update(Workschedule workschedule) {
		return wr.saveAndFlush(workschedule);
	}
}
