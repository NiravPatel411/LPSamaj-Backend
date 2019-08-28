package com.xmplify.starter_kit_springboot_singledb.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xmplify.starter_kit_springboot_singledb.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {


    List<Role> findByNameIn(List<String> roleName);

   // Optional<Set<Role>> findByName(Iterable<String> roleName);
}