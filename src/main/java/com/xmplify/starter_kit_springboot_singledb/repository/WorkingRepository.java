package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.PersonWorking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingRepository extends JpaRepository<PersonWorking, String> {
}