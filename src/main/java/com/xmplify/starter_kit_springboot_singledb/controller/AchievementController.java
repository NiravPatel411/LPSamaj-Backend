package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.AchievementDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.AchievementRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/achievement")
public class AchievementController {
    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllAchievement(){
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.achievementRepository.findAll()),HttpStatus.OK);
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<?> getAchievementById(@PathVariable String achievementId){
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS",this.achievementRepository.findById(achievementId)),HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> AddEducation(@RequestBody AchievementDTO achievementDTO){
        Optional<User> person = userRepository.findById(achievementDTO.getPersonId());
        if(person.isPresent()){
            Achievement achievement = new Achievement();
            achievement.setAchievementDate(achievementDTO.getAchievementDate());
            achievement.setDescription(achievementDTO.getDescription());
            achievement.setPerson(person.get());
            achievement.setProofPhoto(achievementDTO.getProofPhoto());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.achievementRepository.save(achievement)), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "error", null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId){
        Optional<User> user = userRepository.findById(personId);
        if(user.isPresent()){
            Set<Achievement> achievements = user.get().getAchievements();
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", achievements), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "error", null), HttpStatus.NOT_FOUND);
        }
    }
}
