package com.xmplify.starter_kit_springboot_singledb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.xmplify.starter_kit_springboot_singledb.mapper.EducationMapper;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.Address;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.AddressRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.EducationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
	UserMapper userMapper;

    @Autowired
	AddressRepository addressRepository;

    @Autowired
	EducationMapper educationMapper;

	@Autowired
	EducationRepository educationRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public User save(AddPersonDTO addPersonDTO) {
		User user = userMapper.personDetailDTOtoUser(addPersonDTO.getPersonDetail());
		User savedUser = userRepository.save(user);
		List<Address> addresses = new ArrayList<>();
		for(AddAddressFromUserDTO address : addPersonDTO.getAddress()) {
			addresses.add(userMapper.addAddressFromUserDTOtoAddress(address,savedUser));
		}
		List<PersonEducation> educations = new ArrayList<>();
		if(Objects.nonNull(addPersonDTO.getEducation())) {
			for (EducationDTO educationDTO : addPersonDTO.getEducation()) {
				educations.add(educationMapper.educationDTOtoPersonEducation(educationDTO, savedUser));
			}
			savedUser.setEducations(educationRepository.saveAll(educations));
		}
		savedUser.setAddressList(addressRepository.saveAll(addresses));
		return savedUser;
	}




}