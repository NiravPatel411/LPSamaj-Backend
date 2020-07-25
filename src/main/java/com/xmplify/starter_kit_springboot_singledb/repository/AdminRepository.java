package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    @Query("SELECT p FROM Admin p where p.id = :adminId")
    List<Admin> findPersonByAdminId(String adminId, Pageable pageable);

    @Query("SELECT p FROM Admin p where p.person.id = :personId")
    List<Admin> isExistsAdminByPerson(String personId);

    @Query("SELECT p FROM Admin p where p.person.id = :personId AND p.adminRole.name = :role")
    Admin isExistsAdminByPersonAndAdminRole(String personId,String role);

    @Query("SELECT p FROM Admin p where p.adminRole.name = :adminRole")
    List<Admin> AdminByRoles(String adminRole);
}
