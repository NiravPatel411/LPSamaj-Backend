package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRoleRepository extends JpaRepository<AdminRole, String> {


    List<AdminRole> findByNameIn(List<String> roleName);

    AdminRole findByName(String roleName);


    // Optional<Set<Role>> findByName(Iterable<String> roleName);
}
