package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.PersonAchievement;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.AchievementRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.PersonAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementService {
    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    PersonAchievementRepository personAchievementRepository;

    @Autowired
    UserService userService;

    @Transactional
    public ResponseEntity<?> addAchievement(Achievement achievement) {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", achievementRepository.save(achievement)), HttpStatus.OK);
    }

    @Transactional
    public void addPersonAchievement(PersonAchievement personAchievement) {
        personAchievementRepository.save(personAchievement);
        personAchievementRepository.flush();
    }
}
