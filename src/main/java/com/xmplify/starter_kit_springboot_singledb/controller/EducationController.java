package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Achievement;
import com.xmplify.starter_kit_springboot_singledb.model.Education;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public List<Education> getAllEducation(){
        return this.educationRepository.findAll();
    }

    @GetMapping("/{educationId}")
    public Optional<Education> getEducationById(@PathVariable String educationId){
        return this.educationRepository.findById(educationId);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveEducation(@RequestBody EducationDTO educationDTO){
        Optional<User> user = userRepository.findById(educationDTO.getPersonId());
        if(user.isPresent()){
            Education education = new Education();
            education.setDegreeId(educationDTO.getDegreeId());
            education.setEndDate(educationDTO.getEndDate());
            education.setPerson(user.get());
            education.setResult(educationDTO.getResult());
            education.setSchoolName(educationDTO.getSchoolName());
            education.setStartDate(educationDTO.getStartDate());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.educationRepository.save(education)), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "Error", null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId){
        Optional<User> user = userRepository.findById(personId);
        if(user.isPresent()){
            Set<Education> Educations = user.get().getEducations();
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", Educations), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "Error", null), HttpStatus.NOT_FOUND);
        }
    }
}
