package pti.model.service;

import java.util.List;

import pti.model.domain.Admin;

public interface AdminService {
	Admin findById(Long id);

	Admin findByLogin(String login);

	Admin insert(Admin admin);

	Admin update(Admin admin);

	void delete(Admin admin);

	List<Admin> findAll();
}
