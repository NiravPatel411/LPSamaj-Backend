package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonListDTO;
import com.xmplify.starter_kit_springboot_singledb.model.Observation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.ObservationRepository;
import com.xmplify.starter_kit_springboot_singledb.security.SecurityUtils;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/observation")
public class ObservationController {

    @Autowired
    ObservationRepository observationRepository;

    @GetMapping("/observing")
    public ResponseEntity getObserving(){
        List<Observation> observer = observationRepository.findAllByObserver(new User(SecurityUtils.getCurrentUserId()));
        List<PersonListDTO> personListDTOS = new ArrayList<>();
        for(Observation observation : observer){
            User user = observation.getObserving();
            personListDTOS.add(PersonListDTO.create(user));
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personListDTOS), HttpStatus.OK);
    }

    @GetMapping("/observer")
    public ResponseEntity getObserver(){
        List<Observation> observing = observationRepository.findAllByobserving(new User(SecurityUtils.getCurrentUserId()));
        List<PersonListDTO> personListDTOS = new ArrayList<>();
        for(Observation observation : observing){
            User user = observation.getObserver();
            personListDTOS.add(PersonListDTO.create(user));
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS",personListDTOS), HttpStatus.OK);
    }

    @PostMapping("/observe/{userId}")
    public ResponseEntity observe(String userId){
        Observation observation = new Observation(null,new User(SecurityUtils.getCurrentUserId()),new User(userId));
        observationRepository.save(observation);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS",null), HttpStatus.OK);
    }

}
