package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationListDTO;
import com.xmplify.starter_kit_springboot_singledb.model.Address;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.service.EducationService;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import com.xmplify.starter_kit_springboot_singledb.service.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Validators validators;

    @Autowired
    EducationService educationService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public List<PersonEducation> getAllEducation() {
        return this.educationRepository.findAll();
    }

    @GetMapping("/{educationId}")
    public Optional<PersonEducation> getEducationById(@PathVariable String educationId) {
        return this.educationRepository.findById(educationId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEducation(@ModelAttribute EducationListDTO educationListDTO){
        List<String> messages = validators.validateListEducationDTO(educationListDTO.getEducation());
        if(!messages.isEmpty()){
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
        }
        educationService.addEducation(educationListDTO.getEducation());
        return userService.getPersonDetail(educationListDTO.getEducation().get(0).getPersonId());

    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationListById (@PathVariable String personId){
        List<String> messages = new ArrayList<>();
        validators.validateUserId(personId,messages);
        if(!messages.isEmpty()){
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
        }

        Optional<List<PersonEducation>> addressList = educationRepository.findAllByPersonId(personId);
        if(addressList.isPresent()) {
            List<EducationDTO> educationDTO = EducationDTO.create(addressList.get());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", educationDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found any education details ", null), HttpStatus.OK);
        }
    }
}
