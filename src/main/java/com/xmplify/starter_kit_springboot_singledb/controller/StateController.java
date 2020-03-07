package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Country;
import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.model.State;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.CoutryRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.DistrictRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/state")
public class StateController {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @GetMapping("/listStateWithCountry")
    public ResponseEntity<?> listState() {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", stateRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllStates(){
                List<ListStatesResposeDTO> states = stateRepository.getAllStates();
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", states), HttpStatus.OK);
            }

    @PostMapping("/add")
    public  ResponseEntity<?> addState(@RequestBody  StateDTO stateDTO){
        if(Objects.isNull(stateDTO.getStateName()) && StringUtils.isEmpty(stateDTO.getStateName())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"State name should not be blank or null.",null),HttpStatus.BAD_REQUEST);
        }
        if(Objects.isNull(stateDTO.getCountryId()) && StringUtils.isEmpty(stateDTO.getCountryId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"country should not be blank or null.",null),HttpStatus.BAD_REQUEST);
        }
        Optional<Country> country = coutryRepository.findById(stateDTO.getCountryId());
        if(country.isPresent()){
            State statedb = stateRepository.getByNameAndCountry(stateDTO.getCountryId(),stateDTO.getStateName());
            if(Objects.nonNull(statedb)){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(),false,"state already exist in this country",null),HttpStatus.OK);
            }
            State state = new State();
            state.setCountry(country.get());
            state.setName(stateDTO.getStateName());
            State stateSaved = stateRepository.save(state);

            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(),true,"State added",stateRepository.getStatesById(stateSaved.getId())),HttpStatus.OK);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateState(@RequestBody UpdateStateDTO updateStateDTO){
        Optional<State> state = stateRepository.findById(updateStateDTO.getStateId());
        State updatedState = null;
        if(state.isPresent()){
            State updateState = new State();
            updateState.setId(state.get().getId());
            if(Objects.nonNull(updateStateDTO.getStateName())) {
                updateState.setName(updateStateDTO.getStateName());
            } else {
                updateState.setName(state.get().getName());
            }
            if(Objects.nonNull(updateStateDTO.getCountryId())) {
                Optional<Country> country = coutryRepository.findById(updateStateDTO.getCountryId());
                if(!country.isPresent()){
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Country not found",null),HttpStatus.BAD_REQUEST);
                }
                State statedb = stateRepository.getByNameAndCountry(updateStateDTO.getCountryId(),updateStateDTO.getStateName());
                if(Objects.nonNull(statedb) &&  ! statedb.getId().equals(state.get().getId()) && statedb.getCountry().getId().equals(country.get().getId())){
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"State already exist with this country",null),HttpStatus.BAD_REQUEST);
                }
                if( country.isPresent()){
                    updateState.setCountry(country.get());
                }
            } else {
                updateState.setCountry(state.get().getCountry());
            }
            updatedState = stateRepository.save(updateState);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"State not found",null),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(),true,"State updated",stateRepository.getStatesById(updatedState.getId())),HttpStatus.OK);
    }

    @GetMapping("/{stateId}")
    public ResponseEntity<?> getStateById(@PathVariable String stateId){
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", stateRepository.findById(stateId)), HttpStatus.OK);
    }

    @GetMapping("/getDistrict/byState/{stateId}")
    public ResponseEntity<?> getDistrictByStateId(@PathVariable String stateId){
        Optional<State> states = stateRepository.findById(stateId);
        if(states.isPresent()){
            Set<District> districtList = states.get().getDistricts();
            List<DistrictByStateDTO> districtByStateDTOs = new ArrayList<>();
            districtList.stream().forEach((district) -> {
                DistrictByStateDTO districtByStateDTO = new DistrictByStateDTO();
                districtByStateDTO.setDistrictId(district.getId());
                districtByStateDTO.setDistrictName(district.getName());
                districtByStateDTOs.add(districtByStateDTO);
            });
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),true,"SUCCESS",districtByStateDTOs),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),false,"State not found with stateId",null),HttpStatus.OK);
        }

    }


}
