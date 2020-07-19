package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Country;
import com.xmplify.starter_kit_springboot_singledb.model.State;
import com.xmplify.starter_kit_springboot_singledb.payload.AddCountryDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.StatesByCountryDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.CoutryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    CoutryRepository coutryRepository;

    @PostMapping("/")
    public ResponseEntity<?> listCountry() {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", coutryRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping("/addAll")
    public ResponseEntity<?> addAllCountry(@RequestBody List<String> countryRequest) {
        List<Country> countries = new ArrayList<>();
        countryRequest.stream().forEach((name) -> {
            Country country = new Country();
            country.setName(name);
            countries.add(country);
        });
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Country Added", coutryRepository.saveAll(countries)), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCountry(@RequestBody AddCountryDTO addCountryDTO) {
        if (coutryRepository.existsByName(addCountryDTO.getCountryName())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Country Already exist", null), HttpStatus.OK);
        }
        Country country = new Country();
        country.setName(addCountryDTO.getCountryName());
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "country Added", coutryRepository.save(country)), HttpStatus.OK);
    }

    @GetMapping("/{countryId}")
    public ResponseEntity<?> getcountryById(@PathVariable String countryId) {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", coutryRepository.findById(countryId)), HttpStatus.OK);
    }

    @PostMapping("/update/{countryId}")
    public ResponseEntity<?> updateCountry(@PathVariable String countryId, @RequestBody AddCountryDTO addCountryDTO) {
        if (Objects.isNull(addCountryDTO.getCountryName()) && StringUtils.isEmpty(addCountryDTO.getCountryName())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.ALREADY_REPORTED.value(), false, "Country name should not be blank or null.", null), HttpStatus.OK);
        }
        if (!coutryRepository.existsById(countryId)) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Country not found", null), HttpStatus.OK);
        }

        if (coutryRepository.existsByName(addCountryDTO.getCountryName())) {
            Country sameNameCountry = coutryRepository.findByName(addCountryDTO.getCountryName());
            if (!sameNameCountry.getId().equals(countryId)) {
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Country Already exist with another Id", null), HttpStatus.OK);
            }
        }

        Optional<Country> country = coutryRepository.findById(countryId);

        if (!country.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Country not found", null), HttpStatus.OK);
        }

        Country countryObj = country.get();
        countryObj.setName(addCountryDTO.getCountryName());
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "country updated", coutryRepository.save(countryObj)), HttpStatus.OK);
    }


    @GetMapping("/states/{countryId}")
    public ResponseEntity getAllStatesByCountryId(@PathVariable String countryId) {
        Optional<Country> countries = coutryRepository.findById(countryId);
        if (countries.isPresent()) {
            Set<State> states = countries.get().getStates();
            List<StatesByCountryDTO> statesByCountryDTOs = new ArrayList<>();
            states.stream().forEach((state) -> {
                StatesByCountryDTO statesByCountryDTO = new StatesByCountryDTO();
                statesByCountryDTO.setStateId(state.getId());
                statesByCountryDTO.setStateName(state.getName());
                statesByCountryDTOs.add(statesByCountryDTO);
            });
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", statesByCountryDTOs), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Country not found", null), HttpStatus.OK);
        }
    }

//    @PostMapping("/addAll")
//    public  ResponseEntity<?> addAllContry(List<Country> countries){
//        return ResponseEntity.ok(coutryRepository.saveAll(countries));
//    }
}
