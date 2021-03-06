package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Setting.PersonSettingDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.achievement.AchievementDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.*;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.EducationMapper;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
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

    @Autowired
    PersonAchievementRepository personAchievementRepository;


    public List<User> findAll() {
        return userRepository.findAll();
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

    private Specification<User> CreateSpecification(FilterUserDTO filterUser) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final Collection<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(filterUser.getGender())) {
                    final Predicate genderPredicate = cb.equal(root.get(GlobalConstants.GENDER_FIELD), filterUser.getGender());
                    predicates.add(genderPredicate);
                }
                if (!StringUtils.isEmpty(filterUser.getSearchText())) {
                    String text = filterUser.getSearchText();
                    if (!text.contains("%")) {
                        text = "%" + text + "%";
                    }
                    String finalText = text;
                    Predicate searchTextPredicate = cb.or(root.getModel().getDeclaredSingularAttributes().stream()
                            .filter(a -> filterUser.getFreeTextfields().contains(a.getName()))
                            .map(a -> cb.like(root.get(a.getName()), finalText))
                            .toArray(Predicate[]::new));
                    predicates.add(searchTextPredicate);
                }

                if (!StringUtils.isEmpty(filterUser.getMaritalStatus())) {
                    final Predicate maritalStatusPredicate = cb.equal(root.get(GlobalConstants.MARITAL_STATUS_FIELD), filterUser.getMaritalStatus());
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

                if(Objects.nonNull(filterUser.getBloodGroup()) && filterUser.getBloodGroup().length > 0){
                    CriteriaBuilder.In<String> bloodGroupIn = cb.in(root.get(GlobalConstants.BLOOD_GROUP_FIELD));
                    for(String bloodGroup : filterUser.getBloodGroup()){
                        bloodGroupIn.value(bloodGroup);
                    }
                    predicates.add(bloodGroupIn);
                }

                if(Objects.nonNull(filterUser.getBloodDonor()) && Boolean.TRUE.equals(filterUser.getBloodDonor())){
                    Predicate bloodDonate = cb.equal(root.join("personSetting").get(GlobalConstants.BLOOD_DONATE_FIELD), 1);
                    predicates.add(bloodDonate);
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public List<String> getAllHobbies() {
        return GlobalConstants.HOBBIES;
    }

    @Transactional
    public ResponseEntity<?> addPerson(PersonalDetailDTO personalDetailDTO, List<AddressDTO> addressDTOs) {
        Role userRole = roleService.getNormalUserRole();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        PersonSetting savedSetting = personSettingsRepository.save(PersonSetting.getDefaultSetting());
        User user = User.create(personalDetailDTO, userRoles, savedSetting);
        User savedUser = userRepository.save(user);
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO address : addressDTOs) {
            addressList.add(Address.create(address, savedUser));
        }
        List<Address> savedAddress = addressRepository.saveAll(addressList);
        PersonalDetailDTO personalDetailDTO1 = PersonalDetailDTO.create(savedUser);
        PersonSettingDTO personSettingDTO = PersonSettingDTO.create(savedUser.getPersonSetting());
        List<AddressDTO> returnAddressDTO = new ArrayList<>();
        for (Address address : addressList) {
            returnAddressDTO.add(AddressDTO.create(address));
        }

        PersonBasicDetailDTO personBasicDetailDTO = PersonBasicDetailDTO.create(personalDetailDTO1, returnAddressDTO,personSettingDTO);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personBasicDetailDTO), HttpStatus.OK);
    }

    public ResponseEntity<?> updatePerson(PersonalDetailDTO personalDetailDTO, List<AddressDTO> addressDTOs, User oldUser) {
        User user = User.create(personalDetailDTO, oldUser.getRoles(), oldUser.getPersonSetting());
        User savedUser = userRepository.save(user);
        List<Address> addressList = new ArrayList<>();
        for (AddressDTO address : addressDTOs) {
            addressList.add(Address.create(address, savedUser));
        }
        addressRepository.saveAll(addressList);
        PersonalDetailDTO personalDetailDTO1 = PersonalDetailDTO.create(savedUser);
        PersonSettingDTO personSettingDTO = PersonSettingDTO.create(savedUser.getPersonSetting());
        List<AddressDTO> returnAddressDTO = new ArrayList<>();
        for (Address address : addressList) {
            returnAddressDTO.add(AddressDTO.create(address));
        }

        PersonBasicDetailDTO personBasicDetailDTO = PersonBasicDetailDTO.create(personalDetailDTO1, returnAddressDTO, personSettingDTO);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personBasicDetailDTO), HttpStatus.OK);
    }

    public ResponseEntity<?> getPersonDetail(String personId) {
        Optional<User> user = userRepository.findById(personId);


        Optional<List<Address>> addresses = Optional.empty();
        Optional<List<PersonEducation>> educations = Optional.empty();
        Optional<List<User>> familyUsers = Optional.empty();
        Optional<List<PersonAchievement>> personAchievements = Optional.empty();


        if (user.isPresent()) {
            familyUsers = userRepository.findAllByFamilyCode(user.get().getFamilyCode());
            addresses = addressRepository.findByPersonIdId(personId);
            educations = educationRepository.findAllByPersonId(personId);
            personAchievements = personAchievementRepository.findAllByPersonId(personId);
        }

        List<AddressDTO> addressDTOList = null;
        List<EducationDTO> educationDTO = null;
        List<PersonListDTO> familyUsersDTO = null;
        List<AchievementDTO> achievementDTOList = null;

        PersonalDetailDTO personalDetailDTO = PersonalDetailDTO.create(user.get());
        PersonSettingDTO personSettingDTO = PersonSettingDTO.create(user.get().getPersonSetting());

        if (addresses.isPresent()) {
            addressDTOList = AddressDTO.create(addresses.get());
        }
        if (educations.isPresent()) {
            educationDTO = EducationDTO.create(educations.get());
        }
        if(familyUsers.isPresent()){
            familyUsers.get().removeIf((usr) ->  usr.getId().equalsIgnoreCase(personId));
            familyUsersDTO = PersonListDTO.create(familyUsers.get());
        }
        if(personAchievements.isPresent()){
            achievementDTOList = AchievementDTO.create(personAchievements.get());
        }

        PersonDetailDTO personDetailDTO = PersonDetailDTO.create(personalDetailDTO,personSettingDTO, addressDTOList, educationDTO,familyUsersDTO,achievementDTOList);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personDetailDTO), HttpStatus.OK);
    }

    public List<PersonListDTO> getFamilyList(String personId) {
        Optional<List<User>> familyUsers = Optional.empty();
        Optional<User> user = userRepository.findById(personId);
        if (user.isPresent()) {
            familyUsers = userRepository.findAllByFamilyCode(user.get().getFamilyCode());
        }
        List<PersonListDTO> familyUsersDTO = null;
        if(familyUsers.isPresent()){
            familyUsers.get().removeIf((usr) ->  usr.getId().equalsIgnoreCase(personId));
            familyUsersDTO = PersonListDTO.create(familyUsers.get());
        }

        return familyUsersDTO;
    }
}