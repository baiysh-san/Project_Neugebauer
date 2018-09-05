package pti.model.service;

import pti.model.domain.Product;
import pti.model.domain.Workschedule;

import java.util.List;

public interface WorkscheduleService {
	Workschedule findById(Long id);
	Workschedule findByProduct(Product product);
	List<Workschedule> findAll();
	void delete(Workschedule workschedule);
	Workschedule insert(Workschedule workschedule);
	Workschedule update(Workschedule workschedule);
}
