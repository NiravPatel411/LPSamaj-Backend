package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Education;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.model.Working;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.WorkingDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.WorkingRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/working")
public class WorkingController {

    @Autowired
    WorkingRepository workingRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public List<Working> getAllWorking(){
        return this.workingRepository.findAll();
    }

    @GetMapping("/{WorkingId}")
    public ResponseEntity<?> getEducationById(@PathVariable String WorkingId){
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.workingRepository.findById(WorkingId)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveEducation(@RequestBody WorkingDTO workingDTO){
        Optional<User> user = userRepository.findById(workingDTO.getPersonId());
        if(user.isPresent()){
           Working working = new Working();
           working.setAddress(workingDTO.getAddress());
           working.setClass_type(workingDTO.getClass_type());
           working.setDescription(workingDTO.getDescription());
           working.setEndDate(workingDTO.getEndDate());
           working.setOccuption(workingDTO.getOccuption());
           working.setOrganizationName(workingDTO.getOrganizationName());
           working.setPerson(user.get());
           working.setPosition(workingDTO.getPosition());
           working.setProofPhoto(workingDTO.getProofPhoto());
           working.setSector(workingDTO.getSector());
           working.setStartDate(workingDTO.getStartDate());
           return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", workingRepository.save(working)), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "ERROR", null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId){
        Optional<User> user = userRepository.findById(personId);
        if(user.isPresent()){
            Set<Working> workings = user.get().getWorkings();
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", workings), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "ERROR", null), HttpStatus.OK);
        }
    }
}
