package pti.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import pti.model.domain.Admin;
import pti.model.repository.AdminRepository;

@Service("adminService")
@Repository
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository ar;

	@Override
	public Admin findById(Long id) {
		return ar.findOne(id);
	}

	@Override
	public Admin findByLogin(String login) {
		return ar.findByLogin(login);
	}

	@Override
	public Admin insert(Admin admin) {
		return ar.saveAndFlush(admin);
	}

	@Override
	public Admin update(Admin admin) {
		return ar.saveAndFlush(admin);
	}

	@Override
	public void delete(Admin admin) {
		ar.delete(admin);
	}

	@Override
	public List<Admin> findAll() {
		return ar.findAll();
	}

}
