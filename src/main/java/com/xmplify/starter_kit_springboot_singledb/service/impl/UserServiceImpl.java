package com.xmplify.starter_kit_springboot_singledb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xmplify.starter_kit_springboot_singledb.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.Address;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.AddressRepository;
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
		addressRepository.saveAll(addresses);
		return savedUser;
	}


}