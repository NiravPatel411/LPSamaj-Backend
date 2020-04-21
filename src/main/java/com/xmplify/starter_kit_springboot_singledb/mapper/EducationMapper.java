package com.xmplify.starter_kit_springboot_singledb.mapper;

import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EducationMapper {

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateRepository stateRepository;


    public PersonEducation educationDTOtoPersonEducation(EducationDTO educationDTO, User savedUser) {
        PersonEducation personEducation = new PersonEducation(educationDTO.getPersonEducationId(),
                savedUser,
                educationDTO.getDegreeId(),
                educationDTO.getSchoolName(),
                educationDTO.getResult(),
                educationDTO.getStartYear(),
                educationDTO.getEndYear(),
                educationDTO.getProofPhoto(),
                educationDTO.getMedium(),
                educationDTO.getMobileLocalId());
        personEducation.setCreatedBy(adminRepository.findById(educationDTO.getCreatedBy()).get());
        personEducation.setCreatedBy(Objects.nonNull(educationDTO.getCreatedBy())?
                adminRepository.findById(educationDTO.getCreatedBy()).isPresent()?
                        adminRepository.findById(educationDTO.getCreatedBy()).get():
                        null:
                null);
        return personEducation;
    }
}
