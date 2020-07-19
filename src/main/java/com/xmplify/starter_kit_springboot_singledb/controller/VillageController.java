package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.UpdateVillageDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.VillageDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.DistrictRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/village")
public class VillageController {

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    DistrictRepository districtRepository;

    @PostMapping("/")
    public ResponseEntity<?> listUser() {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", villageRepository.getAllVillage()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVillage(@RequestBody VillageDTO villageDTO) {
        if (Objects.isNull(villageDTO.getName()) || StringUtils.isEmpty(villageDTO.getName())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Village should not be blank or null.", null), HttpStatus.OK);
        }
        if (Objects.isNull(villageDTO.getDistrictId()) || StringUtils.isEmpty(villageDTO.getDistrictId())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "District Id should not be blank or null.", null), HttpStatus.OK);
        }
        if(Objects.isNull(villageDTO.getShortForm()) || StringUtils.isEmpty(villageDTO.getShortForm()) || villageRepository.existsByshortForm(villageDTO.getShortForm())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Village Shortform should be Unique.", null), HttpStatus.OK);
        }

        Optional<District> district = districtRepository.findById(villageDTO.getDistrictId());
        if (district.isPresent()) {
            Village village = new Village();
            Optional<Village> villagedb = villageRepository.getVillageByNameAndDistrict_Id(villageDTO.getName(), villageDTO.getDistrictId());
            if (villagedb.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Village already exist with same name in this district", null), HttpStatus.OK);
            }
            village.setDistrict(district.get());
            village.setName(villageDTO.getName());
            village.setShortForm(villageDTO.getShortForm());
            Village villageUpdated = villageRepository.save(village);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "village added", villageRepository.getVillageById(villageUpdated.getId())), HttpStatus.OK);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateVillage(@RequestBody UpdateVillageDTO updateVillageDTO) {
        Optional<Village> village = villageRepository.findById(updateVillageDTO.getVillageId());
        Optional<District> district = null;
        if (!village.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Village not found", null), HttpStatus.OK);
        }
        if (Objects.nonNull(updateVillageDTO.getDistrictId())) {
            district = districtRepository.findById(updateVillageDTO.getDistrictId());
            if (!district.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "district not found", null), HttpStatus.OK);
            }
        }
        if (Objects.nonNull(updateVillageDTO.getVillageName()) && Objects.nonNull(updateVillageDTO.getDistrictId()) && Objects.nonNull(district)) {
            Optional<Village> villageValid = villageRepository.getVillageByNameAndDistrict_Id(updateVillageDTO.getVillageName(), updateVillageDTO.getDistrictId());
            if (villageValid.isPresent() && !villageValid.get().getId().equals(village.get().getId()) && villageValid.get().getDistrict().getId().equals(district.get().getId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "village already exist in this district", null), HttpStatus.OK);
            }
        }

        if(Objects.isNull(updateVillageDTO.getShortForm()) || StringUtils.isEmpty(updateVillageDTO.getShortForm()) || villageRepository.existsByshortForm(updateVillageDTO.getShortForm())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Village Shortform should be Unique.", null), HttpStatus.OK);
        }

        Village updateVillage = new Village();
        if (Objects.nonNull(updateVillageDTO.getVillageName())) {
            updateVillage.setName(updateVillageDTO.getVillageName());
        } else {
            updateVillage.setName(village.get().getName());
        }
        if (Objects.nonNull(updateVillageDTO.getDistrictId())) {
            updateVillage.setDistrict(district.get());
        } else {
            updateVillage.setDistrict(village.get().getDistrict());
        }
        updateVillage.setId(village.get().getId());
        updateVillage.setShortForm(updateVillageDTO.getShortForm());
        Village villageUpdated = villageRepository.save(updateVillage);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "village updated", villageRepository.getVillageById(villageUpdated.getId())), HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addVillage(List<Village> villages) {
        return ResponseEntity.ok(villageRepository.saveAll(villages));
    }
}
