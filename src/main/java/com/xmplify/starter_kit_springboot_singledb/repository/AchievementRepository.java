package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.PersonAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<PersonAchievement, String> {

    //PersonAchievement findBypersonId(String id);
}
