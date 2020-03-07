package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import com.xmplify.starter_kit_springboot_singledb.model.Degree;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    DegreeRepository degreeRepository;

    @Autowired
    CoutryRepository countryRepository;

    @Autowired
    AdminRoleRepository adminRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired

    @GetMapping("/")
    public ResponseEntity<?> getAllData(){
        Map<String, Object> returnMasterObject = new HashMap<>();
        returnMasterObject.put("degree",degreeRepository.findAll());
        returnMasterObject.put("adminTypes",adminRoleRepository.findAll());
        returnMasterObject.put("district",districtRepository.getDistrictList());
        returnMasterObject.put("village",villageRepository.getAllVillage());
        returnMasterObject.put("State",stateRepository.getAllStates());
        returnMasterObject.put("Country",countryRepository.findAll());

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", returnMasterObject), HttpStatus.OK);
    }

}
