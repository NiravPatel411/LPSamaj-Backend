package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public List<PersonEducation> getAllEducation() {
        return this.educationRepository.findAll();
    }

    @GetMapping("/{educationId}")
    public Optional<PersonEducation> getEducationById(@PathVariable String educationId) {
        return this.educationRepository.findById(educationId);
    }


    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId){
        Optional<User> user = userRepository.findById(personId);
        if(user.isPresent()){
            List<PersonEducation> Educations = user.get().getEducations();
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", Educations), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "Error", null), HttpStatus.NOT_FOUND);
        }
    }
}
