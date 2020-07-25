package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonBasicDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.EducationMapper;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDBDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserMapper userMapper;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    EducationMapper educationMapper;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    DegreeRepository degreeRepository;
    @Autowired
    PersonSettingsRepository settingsRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FileService fileService;
    @Autowired
    private PersonSettingsRepository personSettingsRepository;


    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Transactional
    public User save(AddPersonDTO addPersonDTO, HttpServletRequest request) {
        User user = userMapper.personDetailDTOtoUser(addPersonDTO.getPersonDetail(), request);
        User savedUser = userRepository.save(user);
        List<Address> addresses = new ArrayList<>();
        for (AddAddressFromUserDTO address : addPersonDTO.getAddress()) {
            addresses.add(userMapper.addAddressFromUserDTOtoAddress(address, savedUser));
        }
        List<PersonEducation> educations = new ArrayList<>();
        if (Objects.nonNull(addPersonDTO.getEducation())) {
            for (EducationDBDTO educationDTO : addPersonDTO.getEducation()) {
                educations.add(educationMapper.educationDTOtoPersonEducation(educationDTO, savedUser));
            }
            savedUser.setEducations(educationRepository.saveAll(educations));
        }
        savedUser.setAddressList(addressRepository.saveAll(addresses));
        return savedUser;
    }


    public PersonAllDetails getPersonAllServiceByPersonId(String id) {
        PersonAllDetails personAllDetails = new PersonAllDetails();
        PersonalDetail personalDetail = getPersonDetail(id);
        if (!Objects.nonNull(personalDetail)) {
            return null;
        }
        personAllDetails.setPersonalDetails(personalDetail);
        List<AddressDetail> addressDetail = getAddressDetail(id);
        if (Objects.nonNull(addressDetail)) {
            personAllDetails.setAddressDetails(addressDetail);
        }

        List<EducationDTO> educationDTOS = getEducations(id);
        personAllDetails.setEducationDetails(educationDTOS);

        return personAllDetails;
    }


    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    public Page<User> findAll(FilterUserDTO filterUser, Pageable pageable) {
        return userRepository.findAll(CreateSpecification(filterUser), pageable);
    }


    public List<Admin> findAllAdmin() {
        return adminRepository.findAll();
    }


    public List<User> findAllUserByAdminId(String adminId) {
        return userRepository.findAllByAdminId(adminId);
    }


    public List<PersonAllDetails> getPersonAllService(List<User> users) {
        List<PersonAllDetails> personAllDetails = new ArrayList<>();
        for (User user : users) {
            personAllDetails.add(getPersonAllServiceByPersonId(user.getId()));
        }
        return personAllDetails;
    }

    private Specification<User> CreateSpecification(FilterUserDTO filterUser) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final Collection<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(filterUser.getGender())) {
                    final Predicate genderPredicate = cb.equal(root.get(GlobalConstants.GENDER), filterUser.getGender());
                    predicates.add(genderPredicate);
                }
                if (!StringUtils.isEmpty(filterUser.getSearchText())) {
                    String text = filterUser.getSearchText();
                    if (!text.contains("%")) {
                        text = "%" + text + "%";
                    }
                    String finalText = text;
                    Predicate searchTextPredicate = cb.or(root.getModel().getDeclaredSingularAttributes().stream()
                            .filter(a -> filterUser.getfields().contains(a.getName()))
                            .map(a -> cb.like(root.get(a.getName()), finalText))
                            .toArray(Predicate[]::new));
                    predicates.add(searchTextPredicate);
                }

                if (!StringUtils.isEmpty(filterUser.getMaritalStatus())) {
                    final Predicate maritalStatusPredicate = cb.equal(root.get(GlobalConstants.MARITAL_STATUS), filterUser.getMaritalStatus());
                    predicates.add(maritalStatusPredicate);
                }

                if (Objects.nonNull(filterUser.getVillageIds()) && filterUser.getVillageIds().length > 0) {
                    CriteriaBuilder.In<String> inClause = cb.in(root.join("village").get("id"));
                    for (String villageId : filterUser.getVillageIds()) {
                        inClause.value(villageId);
                    }
                    predicates.add(inClause);
                }

                if (Objects.nonNull(filterUser.getDegreeIds()) && filterUser.getDegreeIds().length > 0) {
                    CriteriaBuilder.In<String> educationIn = cb.in(root.join("educations").get("degreeId"));
                    for (String degreeId : filterUser.getDegreeIds()) {
                        educationIn.value(degreeId);
                    }
                    predicates.add(educationIn);
                }

                if (Objects.nonNull(filterUser.getGreaterThanBOD()) && Objects.nonNull(filterUser.getLessThanBOD())) {
                    LocalDate greaterThan = Instant.ofEpochMilli(filterUser.getGreaterThanBOD()).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate lessThan = Instant.ofEpochMilli(filterUser.getLessThanBOD()).atZone(ZoneId.systemDefault()).toLocalDate();

                    Predicate bodPredicate = cb.between(root.get("birthDate"), greaterThan, lessThan);
                    predicates.add(bodPredicate);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    private List<EducationDTO> getEducations(String id) {
        List<PersonEducation> personEducationList = getEducationsFromUserId(id);
        if (!Objects.nonNull(personEducationList)) {
            return null;
        }
        List<EducationDTO> educationDTOList = new ArrayList<>();
        for (PersonEducation personEducation : personEducationList) {
            EducationDTO educationDTO = new EducationDTO(personEducation.getPersonEducationId(), personEducation.getPerson().getId(),
                    personEducation.getDegreeId(), degreeRepository.findById(personEducation.getDegreeId()).get().getName(), personEducation.getSchoolName(), personEducation.getResult(),
                    personEducation.getStartYear(), personEducation.getEndYear(), personEducation.getProofPhoto(),
                    personEducation.getMedium(),
                    Objects.nonNull(personEducation.getCreatedBy()) ? personEducation.getCreatedBy().getId() : "",
                    Objects.nonNull(personEducation.getUpdatedBy()) ? personEducation.getUpdatedBy().getId() : "",
                    Objects.nonNull(personEducation.getDeletedBy()) ? personEducation.getDeletedBy().getId() : "",
                    Objects.nonNull(personEducation.getCreatedAt()) ? personEducation.getCreatedAt().toString() : "",
                    Objects.nonNull(personEducation.getUpdatedAt()) ? personEducation.getUpdatedAt().toString() : "",
                    Objects.nonNull(personEducation.getDeletedAt()) ? personEducation.getDeletedAt().toString() : "",
                    String.valueOf(personEducation.getIsDeleted()), personEducation.getMobileLocalId(),
                    personEducation.getStatus(), "");
            educationDTOList.add(educationDTO);
        }
        return educationDTOList;
    }

    private List<PersonEducation> getEducationsFromUserId(String id) {
        List<PersonEducation> personEducations = educationRepository.findAllByPersonId(id);
        if (!Objects.nonNull(personEducations)) {
            return null;
        }
        return personEducations;
    }

    private List<AddressDetail> getAddressDetail(String id) {
        List<Address> addresses = getAddressFromPersonId(id);
        if (!Objects.nonNull(addresses)) {
            return null;
        }
        List<AddressDetail> addressDetailList = new ArrayList<>();
        for (Address address : addresses) {
            AddressDetail addressDetail = new AddressDetail(address.getId(), address.getPersonId().getId(), address.getAddressType(),
                    address.getAddressText(), address.getCountry().getId(), address.getCountry().getName(), address.getDistrict().getId(), address.getDistrict().getName(),
                    address.getState().getId(), address.getState().getName(), "",
                    Objects.nonNull(address.getCreatedBy()) ? address.getCreatedBy().getId() : "",
                    Objects.nonNull(address.getUpdatedBy()) ? address.getUpdatedBy().getId() : "",
                    Objects.nonNull(address.getCreatedAt()) ? address.getCreatedAt().toString() : "",
                    Objects.nonNull(address.getUpdatedAt()) ? address.getUpdatedAt().toString() : "",
                    Objects.nonNull(address.getDeletedAt()) ? address.getDeletedAt().toString() : "",
                    String.valueOf(address.getIsDeleted()), ""
            );
            addressDetailList.add(addressDetail);
        }
        return addressDetailList;
    }

    private List<Address> getAddressFromPersonId(String id) {
        Optional<List<Address>> optionalAddressList = addressRepository.findByPersonIdId(id);
        if (optionalAddressList.isPresent()) {
            return optionalAddressList.get();
        } else {
            return null;
        }
    }

    private PersonalDetail getPersonDetail(String id) {
        User user = getUserById(id);
        if (Objects.nonNull(user)) {
            return PersonalDetail.toDTO(user);
        } else {
            return null;
        }
    }

    private User getUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }
    }


    public List<String> getAllHobbies() {
        return GlobalConstants.HOBBIES;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<?> addPerson(com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail personalDetail, List<AddressDTO> addressDTOs) {
        Role userRole = roleService.getNormalUserRole();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        PersonSetting savedSetting = personSettingsRepository.save(PersonSetting.getDefaultSetting());
        User user = User.create(personalDetail, userRoles, savedSetting);
        User savedUser = userRepository.save(user);
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO address : addressDTOs) {
            addressList.add(Address.create(address, savedUser));
        }
        List<Address> savedAddress = addressRepository.saveAll(addressList);
        String deleveryPath = fileService.getDeleveryPath(savedUser.getProfilePic(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT);
        com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail personalDetail1 = com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail.create(savedUser, deleveryPath);
        List<AddressDTO> returnAddressDTO = new ArrayList<>();
        for (Address address : addressList) {
            returnAddressDTO.add(AddressDTO.create(address));
        }

        PersonBasicDetailDTO personBasicDetailDTO = PersonBasicDetailDTO.create(personalDetail1, returnAddressDTO);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personBasicDetailDTO), HttpStatus.OK);
    }

    public ResponseEntity<?> updatePerson(com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail personalDetail, List<AddressDTO> addressDTOs, User oldUser) {
        User user = User.create(personalDetail, oldUser.getRoles(), oldUser.getPersonSetting());
        User savedUser = userRepository.save(user);
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO address : addressDTOs) {
            addressList.add(Address.create(address, savedUser));
        }
        addressRepository.saveAll(addressList);
        String deleveryPath = "";
        if (Objects.nonNull(savedUser.getProfilePic())) {
            deleveryPath = fileService.getDeleveryPath(savedUser.getProfilePic(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT);
        }
        com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail personalDetail1 = com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail.create(savedUser, deleveryPath);
        List<AddressDTO> returnAddressDTO = new ArrayList<>();
        for (Address address : addressList) {
            returnAddressDTO.add(AddressDTO.create(address));
        }

        PersonBasicDetailDTO personBasicDetailDTO = PersonBasicDetailDTO.create(personalDetail1, returnAddressDTO);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personBasicDetailDTO), HttpStatus.OK);
    }
}