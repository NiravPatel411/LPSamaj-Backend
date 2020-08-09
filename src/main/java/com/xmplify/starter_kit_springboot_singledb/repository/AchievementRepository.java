package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.PersonAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, String> {

    //PersonAchievement findBypersonId(String id);
    boolean existsByName(String name);

    Optional<Achievement> findByName(String name);
}
