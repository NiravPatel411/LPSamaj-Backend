package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.AddAddressDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.UpdateAddressDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateRepository stateRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllAddressed(){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),true,"SUCCESS",addressRepository.findAll()),HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getAddressByPersonId(@PathVariable String personId){
        if(Objects.isNull(personId) && StringUtils.isEmpty(personId)){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Person Id can be null or blank",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),true,"SUCCESS",addressRepository.findByPersonIdId(personId)),HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveAddress(@RequestBody AddAddressDTO addAddressDTO){
        if(Objects.isNull(addAddressDTO) && Objects.nonNull(addAddressDTO.getPersonId()) && StringUtils.isEmpty(addAddressDTO.getPersonId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Person Id can be null or blank",null),HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(addAddressDTO) && Objects.nonNull(addAddressDTO.getCountryId()) && StringUtils.isEmpty(addAddressDTO.getCountryId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Country Id can be null or blank",null),HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(addAddressDTO) && Objects.nonNull(addAddressDTO.getDistrictId()) && StringUtils.isEmpty(addAddressDTO.getDistrictId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"District Id can be null or blank",null),HttpStatus.BAD_REQUEST);
        }

        Optional<User> person = userRepository.findById(addAddressDTO.getPersonId());
        if(!person.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found person",null),HttpStatus.BAD_REQUEST);
        }

        Optional<Country> country = coutryRepository.findById(addAddressDTO.getCountryId());
        if(!country.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found country",null),HttpStatus.BAD_REQUEST);
        }

        Optional<District> district = districtRepository.findById(addAddressDTO.getDistrictId());
        if(!district.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found District",null),HttpStatus.BAD_REQUEST);
        }

        Optional<State> state = stateRepository.findById(addAddressDTO.getStateId());
        if (!district.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found District", null), HttpStatus.BAD_REQUEST);
        }

        Address address = new Address();
        address.setAddressText(addAddressDTO.getAddressText());
        address.setAddressType(addAddressDTO.getAddressType());
        address.setCountry(country.get());
        address.setDistrict(district.get());
        address.setState(state.get());
        address.setPersonId(person.get());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(),true,"SUCCESS",addressRepository.save(address)),HttpStatus.CREATED);

    }

    @PutMapping("/")
    public ResponseEntity<?> updateAddress(@RequestBody UpdateAddressDTO updateAddressDTO){
        if(Objects.isNull(updateAddressDTO) && Objects.nonNull(updateAddressDTO.getAddressId()) && StringUtils.isEmpty(updateAddressDTO.getAddressId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Address Id can be null or blank",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(updateAddressDTO) && Objects.nonNull(updateAddressDTO.getPersonId()) && StringUtils.isEmpty(updateAddressDTO.getPersonId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Person Id can be null or blank",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(updateAddressDTO) && Objects.nonNull(updateAddressDTO.getCountryId()) && StringUtils.isEmpty(updateAddressDTO.getCountryId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Country Id can be null or blank",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(updateAddressDTO) && Objects.nonNull(updateAddressDTO.getDistrictId()) && StringUtils.isEmpty(updateAddressDTO.getDistrictId())){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"District Id can be null or blank",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        Optional<Address> addressChk = addressRepository.findById(updateAddressDTO.getAddressId());
        if(!addressChk.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found address",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        Optional<User> person = userRepository.findById(updateAddressDTO.getPersonId());
        if(!person.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found person",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        Optional<Country> country = coutryRepository.findById(updateAddressDTO.getCountryId());
        if(!country.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found country",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }

        Optional<District> district = districtRepository.findById(updateAddressDTO.getDistrictId());
        if(!district.isPresent()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false,"Can not found District",addressRepository.findAll()),HttpStatus.BAD_REQUEST);
        }


        Optional<State> state = stateRepository.findById(updateAddressDTO.getStateId());
        if (!district.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found District", null), HttpStatus.BAD_REQUEST);
        }

        Address address = new Address();
        address.setId(updateAddressDTO.getAddressId());
        address.setAddressText(updateAddressDTO.getAddressText());
        address.setAddressType(updateAddressDTO.getAddressType());
        address.setCountry(country.get());
        address.setDistrict(district.get());
        address.setState(state.get());
        address.setPersonId(person.get());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(),true,"Updated",addressRepository.save(address)),HttpStatus.OK);

    }
}
