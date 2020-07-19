package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobileno(String mobileno);

    List<User> findAllByAdminId(String adminid);

    List<User> findByIdIn(List<String> userIds);

    Boolean existsByEmail(String email);

    Boolean existsByMobileno(String mobileno);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    boolean existsByUserName(String userName);

    boolean existsByfamilyCode(String familyCode);
}