package pti.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pti.model.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Admin findByLogin(String login);
}
