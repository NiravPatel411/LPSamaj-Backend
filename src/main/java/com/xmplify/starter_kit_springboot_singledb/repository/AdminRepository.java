package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import com.xmplify.starter_kit_springboot_singledb.model.Person;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    @Query("SELECT p FROM Admin p where p.id = :adminId")
    public List<Admin> findPersonByAdminId (String adminId, Pageable pageable);

    @Query("SELECT p FROM Admin p where p.person.id = :personId")
    public List<Admin> isExistsAdminByPerson(String personId);

    @Query("SELECT p FROM Admin p where p.adminRole.name = :adminRole")
    public List<Admin> AdminByRoles(String adminRole);
}
