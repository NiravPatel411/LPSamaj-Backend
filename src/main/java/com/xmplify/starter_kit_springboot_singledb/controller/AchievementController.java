package com.xmplify.starter_kit_springboot_singledb.controller;

import com.google.common.base.Strings;
import com.xmplify.starter_kit_springboot_singledb.DTOs.achievement.AchievementDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.PersonAchievement;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.AchievementRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.PersonAchievementRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.service.AchievementService;
import com.xmplify.starter_kit_springboot_singledb.service.FileService;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import com.xmplify.starter_kit_springboot_singledb.service.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/achievement")
public class AchievementController {
    @Autowired
    PersonAchievementRepository personAchievementRepository;

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Validators validators;

    @Autowired
    AchievementService achievementService;

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAllAchievement() {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.achievementRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{achievementId}")
    public ResponseEntity<?> getAchievementById(@PathVariable String achievementId) {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.achievementRepository.findById(achievementId)), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAchievement(@RequestBody Achievement achievement) {
        try {
            if (Objects.nonNull(achievement.getId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Id should be null while adding achievement.", null), HttpStatus.NOT_FOUND);
            }
            if (validators.validateAchievementName(achievement.getName())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "achievement name already exist", null), HttpStatus.NOT_FOUND);
            }
            return achievementService.addAchievement(achievement);
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Internal Server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAchievement(@RequestBody Achievement achievement) {
        try {
            Optional<Achievement> existingAchievement = achievementRepository.findById(achievement.getId());
            if(!existingAchievement.isPresent()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "achievement not found by achievement Id", null), HttpStatus.NOT_FOUND);
            }
            if (!validators.validateAchievementNameWithId(existingAchievement.get().getId(), achievement.getName())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "achievement name already exist", null), HttpStatus.NOT_FOUND);
            }
            return achievementService.addAchievement(achievement);
        } catch (Exception e){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Internal Server error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/person/add")
    public ResponseEntity<?> addPersonAchievement(@ModelAttribute AchievementDTO achievementDTO, HttpServletRequest request){
        List<String> messages = validators.validateAchievementDto(achievementDTO);
        if(!messages.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "invalid request", messages.toString()), HttpStatus.NOT_FOUND);
        }
        Optional<PersonAchievement> oldPersonAchievement = Optional.empty();
        if(Objects.nonNull(achievementDTO.getId())){
            oldPersonAchievement = personAchievementRepository.findById(achievementDTO.getId());
        }

        PersonAchievement personAchievement = PersonAchievement.create(achievementDTO);
        if(Objects.nonNull(achievementDTO.getProofPhoto())){
            if(oldPersonAchievement.isPresent() && Objects.nonNull(oldPersonAchievement.get().getProofPhoto())){
                if (!fileService.deleteFile(oldPersonAchievement.get().getProofPhoto(), GlobalConstants.IMAGE, GlobalConstants.ACHIEVEMENT_EVENT, request.getServletContext())) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Problem to delete old Image. see logs", null), HttpStatus.OK);
                }
                personAchievement.setProofPhoto(null);
            }
            String fileStorePath = fileService.uploadFile(achievementDTO.getProofPhoto(),request.getServletContext(), GlobalConstants.IMAGE,GlobalConstants.ACHIEVEMENT_EVENT);
            personAchievement.setProofPhoto(fileStorePath);

        } else if(oldPersonAchievement.isPresent()){
            personAchievement.setProofPhoto(oldPersonAchievement.get().getProofPhoto());
        }

        achievementService.addPersonAchievement(personAchievement);
        return userService.getPersonDetail(personAchievement.getPerson().getId());
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId) {
        if(Strings.isNullOrEmpty(personId)){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "person id can not be null or empty", null), HttpStatus.NOT_FOUND);
        }
        Optional<List<PersonAchievement>> achievements = personAchievementRepository.findAllByPersonId(personId);
        if (!achievements.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "No acheivement found", null), HttpStatus.NOT_FOUND);
        }
        List<AchievementDTO> achievementDTOList = AchievementDTO.create(achievements.get());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", achievementDTOList), HttpStatus.OK);
    }
}
