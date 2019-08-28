package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/village")
public class VillageController {

    @Autowired
    VillageRepository villageRepository;

    @PostMapping("/")
    public ResponseEntity<?> listUser() {
        return ResponseEntity.ok(villageRepository.findAll());
    }

    @PostMapping("/add")
    public  ResponseEntity<?> addVillage(Village village){
        return ResponseEntity.ok(villageRepository.save(village));
    }

    @PostMapping("/addAll")
    public  ResponseEntity<?> addVillage(List<Village> villages){
        return ResponseEntity.ok(villageRepository.saveAll(villages));
    }
}
