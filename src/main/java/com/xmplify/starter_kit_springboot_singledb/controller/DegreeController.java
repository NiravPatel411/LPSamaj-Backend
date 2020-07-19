package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Degree;
import com.xmplify.starter_kit_springboot_singledb.payload.AddDegreeDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.UpdateDegreeDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/Degree")
public class DegreeController {

    @Autowired
    DegreeRepository degreeRepository;

    @GetMapping("/")
    public ResponseEntity<?> getAllDegree() {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", degreeRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDegree(@RequestBody AddDegreeDTO addDegreeDTO) {
        Optional<Degree> degreeDb = degreeRepository.findByName(addDegreeDTO.getDegreeName());
        if (degreeDb.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Degree already exist", null), HttpStatus.OK);
        }
        Degree degree = new Degree();
        degree.setName(addDegreeDTO.getDegreeName());
        degree.setProofNedded(addDegreeDTO.isProofNedded());

        Object obj = degreeRepository.save(degree);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Degree Added", obj), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> addDegree(@RequestBody UpdateDegreeDTO updateDegreeDTO) {
        Optional<Degree> degreeDb = degreeRepository.findById(updateDegreeDTO.getDegreeId());
        if (!degreeDb.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Degree not found", null), HttpStatus.OK);
        }
        Degree degree = new Degree();
        degree.setName(updateDegreeDTO.getDegreeName());
        degree.setProofNedded(updateDegreeDTO.isProofNedded());
        degree.setId(degreeDb.get().getId());

        Object obj = degreeRepository.save(degree);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Degree updated", obj), HttpStatus.OK);
    }
}
