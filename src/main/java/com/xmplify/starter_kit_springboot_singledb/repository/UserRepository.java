package com.xmplify.starter_kit_springboot_singledb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xmplify.starter_kit_springboot_singledb.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameOrMobileno(String username, String mobileno);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findByEmail(String email);
	
	Optional<User> findByMobileno(String mobileno);

	List<User> findByIdIn(List<Long> userIds);

	Boolean existsByEmail(String email);
	
	Boolean existsByMobileno(String mobileno);

}