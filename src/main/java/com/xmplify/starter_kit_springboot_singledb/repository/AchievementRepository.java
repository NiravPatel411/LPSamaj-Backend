package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, String> {

    //Achievement findBypersonId(String id);
}
