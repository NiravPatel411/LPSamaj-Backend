package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {


    List<Role> findByNameIn(List<String> roleName);

    Optional<Role> findByNameContainingIgnoreCase(String name);

    // Optional<Set<Role>> findByName(Iterable<String> roleName);
}