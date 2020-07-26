package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EducationService {

    @Autowired
    EducationRepository educationRepository;

    @Transactional
    public void addEducation(List<EducationDTO> educationDTO) {
        List<PersonEducation> personEducationList = PersonEducation.create(educationDTO);
        educationRepository.saveAll(personEducationList);
    }
}
