package com.xmplify.starter_kit_springboot_singledb.mapper;

import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDBDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.service.RoleService;
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

}
