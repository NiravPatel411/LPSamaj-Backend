package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.model.State;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.DistrictDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.UpdateDistrictDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.VillageByDistrictDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.DistrictRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/district")
public class DistrictController {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateRepository stateRepository;

    @PostMapping("/")
    public ResponseEntity<?> listDistrict() {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", districtRepository.getDistrictList()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDistrict(@RequestBody DistrictDTO districtDTO) {
        if (Objects.isNull(districtDTO.getName()) && StringUtils.isEmpty(districtDTO.getName())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "District name should not be blank or null.", null), HttpStatus.OK);
        }
        if (Objects.isNull(districtDTO.getStateId()) && StringUtils.isEmpty(districtDTO.getStateId())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "stateId should not be blank or null.", null), HttpStatus.OK);
        }
        Optional<State> state = stateRepository.findById(districtDTO.getStateId());
        if (state.isPresent()) {
            Optional<District> districtdb = districtRepository.getByDistrictNameAndStateId(districtDTO.getName(), state.get().getId());
            if (districtdb.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "district already exist in this state", null), HttpStatus.OK);
            }
            District district = new District();
            district.setName(districtDTO.getName());
            district.setState(state.get());
            District districtUpdated = districtRepository.save(district);
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "district added", districtRepository.getDistrictById(districtUpdated.getId())), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDistrict(@RequestBody UpdateDistrictDTO updateDistrictDTO) {
        Optional<District> district = districtRepository.findById(updateDistrictDTO.getDistrictId());
        if (!district.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "district not found", null), HttpStatus.BAD_REQUEST);
        }
        Optional<State> state = null;
        if (Objects.nonNull(updateDistrictDTO.getStateId())) {
            state = stateRepository.findById(updateDistrictDTO.getStateId());
            if (!state.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "state not found", null), HttpStatus.BAD_REQUEST);
            }


            Optional<District> districtValid = districtRepository.getByDistrictNameAndStateId(updateDistrictDTO.getDistrictName(), updateDistrictDTO.getStateId());
            if (districtValid.isPresent() && !districtValid.get().getId().equals(district.get().getId()) && districtValid.get().getState().getId().equals(state.get().getId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "district already exist in this state", null), HttpStatus.OK);
            }
        } else {
            Optional<District> districtValid = districtRepository.getByDistrictNameAndStateId(district.get().getName(), district.get().getState().getId());
            if (districtValid.isPresent() && !districtValid.get().getId().equals(district.get().getId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "district already exist in this state", null), HttpStatus.OK);
            }
        }

        District updateDistrict = new District();
        updateDistrict.setId(district.get().getId());
        if (Objects.nonNull(state) && Objects.nonNull(state.get())) {
            updateDistrict.setState(state.get());
        } else {
            updateDistrict.setState(district.get().getState());
        }
        if (Objects.nonNull(updateDistrictDTO.getDistrictName())) {
            updateDistrict.setName(updateDistrictDTO.getDistrictName());
        } else {
            updateDistrict.setName(district.get().getName());
        }
        District districtUpdated = districtRepository.save(updateDistrict);

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "district updated", districtRepository.getDistrictById(districtUpdated.getId())), HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addAllDistrict(List<District> districts) {
        return ResponseEntity.ok(districtRepository.saveAll(districts));
    }

    @GetMapping("/getVillage/byDistrict/{districtId}")
    public ResponseEntity<?> getDistrictByStateId(@PathVariable String districtId) {
        Optional<District> district = districtRepository.findById(districtId);
        if (district.isPresent()) {
            Set<Village> villageList = district.get().getVillage();
            List<VillageByDistrictDTO> villageByDistrictDTOS = new ArrayList<>();
            villageList.stream().forEach((village) -> {
                VillageByDistrictDTO districtByStateDTO = new VillageByDistrictDTO();
                districtByStateDTO.setVillageId(village.getId());
                districtByStateDTO.setVillageName(village.getName());
                villageByDistrictDTOS.add(districtByStateDTO);
            });
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", villageByDistrictDTOS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "District not found with stateId", null), HttpStatus.OK);
        }

    }

}
