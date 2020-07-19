package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.PersonWorking;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.WorkingDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.WorkingRepository;
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
    public List<PersonWorking> getAllWorking() {
        return this.workingRepository.findAll();
    }

    @GetMapping("/{WorkingId}")
    public ResponseEntity<?> getEducationById(@PathVariable String WorkingId) {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", this.workingRepository.findById(WorkingId)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveEducation(@RequestBody WorkingDTO workingDTO) {
        Optional<User> user = userRepository.findById(workingDTO.getPersonId());
        if (user.isPresent()) {
            PersonWorking personWorking = new PersonWorking();
            personWorking.setAddress(workingDTO.getAddress());
            personWorking.setClassType(workingDTO.getClass_type());
            personWorking.setDescription(workingDTO.getDescription());
            personWorking.setEndDate(workingDTO.getEndDate());
            personWorking.setOccuption(workingDTO.getOccuption());
            personWorking.setOrganizationName(workingDTO.getOrganizationName());
            personWorking.setPerson(user.get());
            personWorking.setPosition(workingDTO.getPosition());
            personWorking.setProofPhoto(workingDTO.getProofPhoto());
            personWorking.setSector(workingDTO.getSector());
            personWorking.setStartDate(workingDTO.getStartDate());
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", workingRepository.save(personWorking)), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "ERROR", null), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<?> getEducationByPerson(@PathVariable String personId) {
        Optional<User> user = userRepository.findById(personId);
        if (user.isPresent()) {
            Set<PersonWorking> personWorkings = user.get().getPersonWorkings();
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", personWorkings), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "ERROR", null), HttpStatus.OK);
        }
    }
}
