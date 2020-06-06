package com.xmplify.starter_kit_springboot_singledb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.xmplify.starter_kit_springboot_singledb.mapper.EducationMapper;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.Address;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.payload.AddressDetail;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonAllDetails;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonalDetail;
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

	@Override
	public PersonAllDetails getPersonAllServiceByPersonId(String id) {
		PersonAllDetails personAllDetails = new PersonAllDetails();
		PersonalDetail personalDetail = getPersonDetail(id);
		if(!Objects.nonNull(personalDetail)){
			return null;
		}
		personAllDetails.setPersonalDetails(personalDetail);
		List<AddressDetail> addressDetail = getAddressDetail(id);
		if(Objects.nonNull(addressDetail)){
			personAllDetails.setAddressDetails(addressDetail);
		}

		List<EducationDTO> educationDTOS = getEducations(id);
		personAllDetails.setEducationDetails(educationDTOS);
		return personAllDetails;
	}

	private List<EducationDTO> getEducations(String id) {
		List<PersonEducation> personEducationList = getEducationsFromUserId(id);
		if(!Objects.nonNull(personEducationList)){
			return null;
		}
		List<EducationDTO> educationDTOList = new ArrayList<>();
		for(PersonEducation personEducation : personEducationList){
			EducationDTO educationDTO = new EducationDTO(personEducation.getPersonEducationId(),personEducation.getPerson().getId(),
					personEducation.getDegreeId(),personEducation.getSchoolName(),personEducation.getResult(),
					personEducation.getStartYear(),personEducation.getEndYear(), personEducation.getProofPhoto(),
					personEducation.getMedium(),
					Objects.nonNull(personEducation.getCreatedBy()) ? personEducation.getCreatedBy().getId() : "",
					Objects.nonNull(personEducation.getUpdatedBy()) ? personEducation.getUpdatedBy().getId() : "",
					Objects.nonNull(personEducation.getDeletedBy()) ? personEducation.getDeletedBy().getId() : "",
					Objects.nonNull(personEducation.getCreatedAt()) ? personEducation.getCreatedAt().toString() : "",
					Objects.nonNull(personEducation.getUpdatedAt()) ? personEducation.getUpdatedAt().toString() : "",
					Objects.nonNull(personEducation.getDeletedAt()) ? personEducation.getDeletedAt().toString() : "",
					String.valueOf(personEducation.getIsDeleted()),personEducation.getMobileLocalId(),
					personEducation.getStatus(),"");
			educationDTOList.add(educationDTO);
		}
		return educationDTOList;
	}

	private List<PersonEducation> getEducationsFromUserId(String id) {
		List<PersonEducation> personEducations = educationRepository.findAllByPersonId(id);
		if(!Objects.nonNull(personEducations)){
			return null;
		}
		return  personEducations;
	}

	private List<AddressDetail> getAddressDetail(String id) {
		List<Address> addresses = getAddressFromPersonId(id);
		if(!Objects.nonNull(addresses)){
			return null;
		}
		List<AddressDetail> addressDetailList =  new ArrayList<>();
		for(Address address : addresses){
			AddressDetail addressDetail = new AddressDetail(address.getId(),address.getPersonId().getId(),address.getAddressType(),
					address.getAddressText(),address.getCountry().getId(),address.getDistrict().getId(),
					address.getState().getId(),"",
					Objects.nonNull(address.getCreatedBy()) ? address.getCreatedBy().getId() : "",
					Objects.nonNull(address.getUpdatedBy()) ? address.getUpdatedBy().getId() : "",
					Objects.nonNull(address.getCreatedAt()) ? address.getCreatedAt().toString() : "",
					Objects.nonNull(address.getUpdatedAt()) ? address.getUpdatedAt().toString() : "",
					Objects.nonNull(address.getDeletedAt()) ? address.getDeletedAt().toString() : "",
					String.valueOf(address.getIsDeleted()),""
					);
			addressDetailList.add(addressDetail);
		}
		return addressDetailList;
	}

	private List<Address> getAddressFromPersonId(String id) {
		Optional<List<Address>> optionalAddressList = addressRepository.findByPersonIdId(id);
		if(optionalAddressList.isPresent()){
			return optionalAddressList.get();
		} else {
			return null;
		}
	}

	private PersonalDetail getPersonDetail(String id) {
		User user = getUserById(id);
		if(Objects.nonNull(user)){
			return new PersonalDetail(user.getId(),user.getFirstName(),user.getLastName(),
					user.getHusbandVillageId(),
					user.getHusbandFirstName(),
					user.getHusbandLastName(),
					user.getHusbandSurname(),
					user.getSurname(),
					user.getStatus(),
					user.getProfilePic(),
					user.getHusbandVillageId(),
					user.getEmail(),
					user.getGender(),
					user.getBirthDate().toString(),
					user.getBloodGroup(),
					user.getMaritalStatus(),user.getMobileno(),
					"",
					Objects.nonNull(user.getCreatedBy()) ? user.getCreatedBy().getId() : "",
					Objects.nonNull(user.getUpdatedBy()) ? user.getUpdatedBy().getId() : "",
					Objects.nonNull(user.getCreatedAt()) ? user.getCreatedAt().toString() : "",
					Objects.nonNull(user.getUpdatedAt()) ? user.getUpdatedAt().toString() : "",
					Objects.nonNull(user.getDeletedAt()) ? user.getDeletedAt().toString() : "",
					String.valueOf(user.getIsDeleted()),"");
		} else {
			return null;
		}
	}

	private User getUserById(String id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if(optionalUser.isPresent()){
			return optionalUser.get();
		} else {
			return null;
		}
	}


}