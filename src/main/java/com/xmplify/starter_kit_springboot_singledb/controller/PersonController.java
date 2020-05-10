package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdatePersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateUserDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import com.xmplify.starter_kit_springboot_singledb.service.impl.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminRoleRepository adminRoleRepository;

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    Validators validators;

    @Autowired
    EducationRepository educationRepository;

    @GetMapping("/")
    public ResponseEntity<?> listUser(@PageableDefault(page = 1)Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<ListPersonBasicDetail> listPersonBasicDetails = new ArrayList<>();
        users.getContent().forEach(user -> {

            ListPersonBasicDetail listPersonBasicDetail = new ListPersonBasicDetail();
            listPersonBasicDetail.setEmail(user.getEmail());
            listPersonBasicDetail.setFirstName(user.getFirstName());
            listPersonBasicDetail.setGender(user.getGender());
            listPersonBasicDetail.setLastName(user.getLastName());
            listPersonBasicDetail.setMobileno(user.getMobileno());
            listPersonBasicDetail.setPersonId(user.getId());
            //listPersonBasicDetail.setProfilePic(user.getProfilePic());

            //todo : remove temp string in setProfilePic
            listPersonBasicDetail.setProfilePic("");
            listPersonBasicDetail.setSurname(user.getSurname());

            listPersonBasicDetail.setHusbandVillageName(user.getHusbandVillageId() != null ? user.getHusbandVillageId() : "");
            listPersonBasicDetail.setHusbandFirstName(user.getHusbandFirstName() != null ? user.getHusbandFirstName() : "");
            listPersonBasicDetail.setHusbandLastName(user.getHusbandLastName() != null ? user.getHusbandLastName() : "");
            listPersonBasicDetail.setHusbandSurname(user.getHusbandSurname() != null ? user.getHusbandSurname() : "");


            listPersonBasicDetail.setCreatedDate(Objects.nonNull(user.getCreatedAt())?user.getCreatedAt().toString():"");
            listPersonBasicDetail.setUpdatedDate(Objects.nonNull(user.getUpdatedAt())?user.getUpdatedAt().toString():"");
            listPersonBasicDetail.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
            listPersonBasicDetail.setUpdatedBy(user.getUpdatedBy() != null ? user.getUpdatedBy().getId() : null);
            listPersonBasicDetail.setIsDeleted(user.getIsDeleted());
            listPersonBasicDetail.setStatus(user.getStatus());
            if (user.getVillage() != null) {
                listPersonBasicDetail.setVillageName(user.getVillage().getName());
                listPersonBasicDetail.setHusbandVillageName(user.getVillage() != null ? user.getVillage().getName() : "");
            }

            listPersonBasicDetails.add(listPersonBasicDetail);
        });

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", listPersonBasicDetails), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUser(@RequestParam String searchText, final Pageable pageable) {
        List<String> fields = new ArrayList<>();
        fields.add("firstName");
        fields.add("lastName");
        fields.add("surname");
        fields.add("email");
        fields.add("gender");
        fields.add("mobileno");
        Page<User> users = userRepository.findAll(textInAllColumns(searchText, fields), pageable);
        List<ListPersonBasicDetail> listPersonBasicDetails = new ArrayList<>();
        users.forEach(user -> {
            ListPersonBasicDetail listPersonBasicDetail = new ListPersonBasicDetail();

            listPersonBasicDetail.setEmail(user.getEmail());
            listPersonBasicDetail.setFirstName(user.getFirstName());
            listPersonBasicDetail.setGender(user.getGender());
            listPersonBasicDetail.setLastName(user.getLastName());
            listPersonBasicDetail.setMobileno(user.getMobileno());
            listPersonBasicDetail.setPersonId(user.getId());
            listPersonBasicDetail.setProfilePic(user.getProfilePic());
            listPersonBasicDetail.setSurname(user.getSurname());

            listPersonBasicDetail.setHusbandVillageName(user.getHusbandVillageId() != null ? user.getHusbandVillageId() : "");
            listPersonBasicDetail.setHusbandFirstName(user.getHusbandFirstName() != null ? user.getHusbandFirstName() : "");
            listPersonBasicDetail.setHusbandLastName(user.getHusbandLastName() != null ? user.getHusbandLastName() : "");
            listPersonBasicDetail.setHusbandSurname(user.getHusbandSurname() != null ? user.getHusbandSurname() : "");


            listPersonBasicDetail.setCreatedDate(user.getCreatedAt().toString());
//            listPersonBasicDetail.setUpdatedDate(user.getUpdatedAt().toString());
            listPersonBasicDetail.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
//            listPersonBasicDetail.setUpdatedBy(user.getUpdatedBy().getId());
            listPersonBasicDetail.setIsDeleted(user.getIsDeleted());
            listPersonBasicDetail.setStatus(user.getStatus());
            if (user.getVillage() != null) {
                listPersonBasicDetail.setVillageName(user.getVillage().getName());
            }

            listPersonBasicDetails.add(listPersonBasicDetail);
        });

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", listPersonBasicDetails), HttpStatus.OK);
    }

    private Specification<User> textInAllColumns(String text, List<String> attributes) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        String finalText = text;
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.or(root.getModel().getDeclaredSingularAttributes().stream()
                .filter(a -> attributes.contains(a.getName()))
                .map(a -> builder.like(root.get(a.getName()), finalText))
                .toArray(Predicate[]::new)
        );
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute UpdateUserDTO updateUserDTO, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            getError(result,errors);
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }
        if (updateUserDTO.getPersonDetail().getPersonId() != null && userRepository.existsById(updateUserDTO.getPersonDetail().getPersonId())) {
            ResponseEntity<?> responseEntity = this.updateUser(updateUserDTO, result);
            return responseEntity;
        } else {
            AddPersonDTO addPersonDTO = userMapper.updateUserDTOToAddUserDTO(updateUserDTO);
            List<String> messages = validators.validateAddPersonDTO(addPersonDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, messages.toString(), null),
                        HttpStatus.BAD_REQUEST);
            }
            User savedUser = userService.save(addPersonDTO);
//            Optional<User> OptionalFromDbUser = userRepository.findById(savedUser.getId());
//            if (!OptionalFromDbUser.isPresent()) {
//                return null;
//            }
//            User fromDbUser = OptionalFromDbUser.get();
//            Optional<List<Address>> address = addressRepository.findByPersonIdId(fromDbUser.getId());
//            if (address.isPresent()) {
//                fromDbUser.setAddressList(address.get());
//            }

            return new ResponseEntity(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userMapper.userToAddPersonResponse(savedUser, addPersonDTO)), HttpStatus.CREATED);

        }
    }

    private void getError(BindingResult result, List<String> errors) {
        for (Object object : result.getAllErrors()) {
            if (object instanceof FieldError) {
                FieldError fieldError = (FieldError) object;
                System.out.println(fieldError.getCode());
                errors.add(fieldError.getDefaultMessage());
            }
            if (object instanceof ObjectError) {
                ObjectError objectError = (ObjectError) object;

                System.out.println(objectError.getCode());
                errors.add(objectError.getDefaultMessage());
            }
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @ModelAttribute UpdateUserDTO updateUserDTO, BindingResult result) {
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (Object object : result.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    System.out.println(fieldError.getCode());
                    errors.add(fieldError.getDefaultMessage());
                }
                if (object instanceof ObjectError) {
                    ObjectError objectError = (ObjectError) object;

                    System.out.println(objectError.getCode());
                    errors.add(objectError.getDefaultMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }

        if (!userRepository.existsById(updateUserDTO.getPersonDetail().getPersonId())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "can not found person", null), HttpStatus.BAD_REQUEST);
        }

        List<String> addressErrors = new ArrayList<>();
        updateUserDTO.getAddress().forEach((address) -> {
            if (coutryRepository.findById(address.getCountryId()).isPresent()) {
                addressErrors.add("Can not found country with " + address.getCountryId() + " Id");
            }
            if (districtRepository.findById(address.getDistrictId()).isPresent()) {
                addressErrors.add("Can not found District with " + address.getDistrictId() + "Id");
            }
        });

        if (Objects.isNull(addressErrors)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found country/district", addressErrors), HttpStatus.BAD_REQUEST);
        }

        if (updateUserDTO.getPersonDetail().isSync()) {
            String resp = updatePersonDetail(updateUserDTO);
            if (!StringUtils.isEmpty(resp)) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, resp, null), HttpStatus.BAD_REQUEST);
            }
        }

        Optional<User> user = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId());
        if (!user.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "some thing went wrong", null), HttpStatus.BAD_REQUEST);
        }

        List<Address> returnAddress = new ArrayList<>();
        for (UpdateAddressFromUserDTO address : updateUserDTO.getAddress()) {
            if (address.isSync()) {
                Optional<Admin> createdBy = adminRepository.findById(address.getCreatedBy());
                if (!createdBy.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "createdBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }

                Optional<Admin> updatedBy = adminRepository.findById(address.getUpdatedBy());
                if (!updatedBy.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "updatedBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }
                returnAddress.add(updatePersonAddress(address, user.get(), createdBy.get(), updatedBy.get()));
            }
        }
        List<PersonEducation> educationList = new ArrayList<>();

        for (EducationDTO education : updateUserDTO.getEducation()) {
            if (education.getIsSync().equalsIgnoreCase("true")) {
                Optional<Admin> createdBy = adminRepository.findById(education.getCreatedBy());
                if (!createdBy.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "createdBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }

                Optional<Admin> updatedBy = adminRepository.findById(education.getUpdatedBy());
                if (!updatedBy.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "updatedBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }
                educationList.add(updateEducation(education, user.get(), createdBy.get(), updatedBy.get()));
            }
        }

        userRepository.flush();
        Optional<User> updatedUser = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId());
        GetPersonDetail getPersonDetail = new GetPersonDetail();
        List<GetAddressDetail> addressDetailList = new ArrayList<>();
        List<EducationDTO> educationDTOList = new ArrayList<>();
        if (updatedUser.isPresent()) {
            getPersonDetail.setAdminId(updatedUser.get().getAdminId());
            getPersonDetail.setAdminName(updatedUser.get().getAdmin().getName());
            getPersonDetail.setBirthDate(updatedUser.get().getBirthDate().toString());
            getPersonDetail.setEmail(updatedUser.get().getEmail());
            getPersonDetail.setFirstName(updatedUser.get().getFirstName());
            getPersonDetail.setGender(updatedUser.get().getGender());
            getPersonDetail.setLastName(updatedUser.get().getLastName());
            getPersonDetail.setMaritualStatus(updatedUser.get().getMaritalStatus());
            getPersonDetail.setMobileno(updatedUser.get().getMobileno());
            getPersonDetail.setBloodGroup(updatedUser.get().getBloodGroup());

            getPersonDetail.setCreatedBy(updatedUser.get().getCreatedBy().getId());
            getPersonDetail.setCreatedDate(Objects.nonNull(updatedUser.get().getCreatedAt()) ? updatedUser.get().getCreatedAt().toString() : "");
            getPersonDetail.setUpdatedDate(Objects.nonNull(updatedUser.get().getUpdatedAt()) ? updatedUser.get().getUpdatedAt().toString() : "");
            getPersonDetail.setUpdatedBy(Objects.nonNull(updatedUser.get().getUpdatedBy()) ? updatedUser.get().getUpdatedBy().getId() : "");
            getPersonDetail.setStatus(updatedUser.get().getStatus());
            getPersonDetail.setIsDeleted(updatedUser.get().getIsDeleted());
            getPersonDetail.setMobileLocalId(updateUserDTO.getPersonDetail().getMobileLocalId());
            getPersonDetail.setPersonId(updatedUser.get().getId());
            // user.setProfilePic(addPersonDTO.getPersonDetail().getProfilePic());
            getPersonDetail.setSurname(updatedUser.get().getSurname());


            getPersonDetail.setHusbandVillageId(updatedUser.get().getHusbandVillageId() != null ? updatedUser.get().getHusbandVillageId() : "");
            getPersonDetail.setHusbandFirstName(updatedUser.get().getHusbandFirstName() != null ? updatedUser.get().getHusbandFirstName() : "");
            getPersonDetail.setHusbandLastName(updatedUser.get().getHusbandLastName() != null ? updatedUser.get().getHusbandLastName() : "");
            getPersonDetail.setHusbandSurname(updatedUser.get().getHusbandSurname() != null ? updatedUser.get().getHusbandSurname() : "");


            getPersonDetail.setVillageName(updatedUser.get().getVillage().getName());

            Optional<List<Address>> addressFromDB = addressRepository.findByPersonIdId(updatedUser.get().getId());
            if (returnAddress != null) {
                for (Address address : returnAddress) {
                    GetAddressDetail getAddress = new GetAddressDetail();
                    getAddress.setId(address.getId());
                    getAddress.setAddressText(address.getAddressText());
                    getAddress.setAddressType(address.getAddressType());
                    getAddress.setCountry(Objects.nonNull(address.getCountry()) ? address.getCountry().getName() : "");
                    getAddress.setCountryId(Objects.nonNull(address.getCountry()) ? address.getCountry().getId() : "");
                    getAddress.setDistrict(Objects.nonNull(address.getDistrict()) ? address.getDistrict().getName() : "");
                    getAddress.setDistrictId(Objects.nonNull(address.getDistrict()) ? address.getDistrict().getId() : "");
                    getAddress.setState(Objects.nonNull(address.getState()) ? address.getState().getName() : "");
                    getAddress.setStateId(Objects.nonNull(address.getState()) ? address.getState().getId() : "");
                    getAddress.setPersonId(updatedUser.get().getId());
                    getAddress.setMobileLocalId(address.getMobileLocalId());
                    getAddress.setCreatedBy(address.getCreatedBy().getId());
                    getAddress.setCreatedDate(Objects.nonNull(address.getCreatedAt()) ? updatedUser.get().getCreatedAt().toString() : "");
                    getAddress.setUpdatedBy(address.getUpdatedBy().getId());
                    getAddress.setUpdatedDate(Objects.nonNull(address.getUpdatedAt()) ? updatedUser.get().getUpdatedAt().toString() : "");
                    getAddress.setStatus(address.getStatus());
                    getAddress.setIsDeleted(address.getIsDeleted());
                    getAddress.setMobileLocalId(address.getMobileLocalId());
                    addressDetailList.add(getAddress);
                }
            }
            if (educationList != null) {
                for (PersonEducation education : educationList) {
                    EducationDTO educationDTO = new EducationDTO();
                    educationDTO.setPersonEducationId(education.getPersonEducationId());
                    educationDTO.setDegreeId(education.getDegreeId());
                    educationDTO.setStartYear(education.getStartYear());
                    educationDTO.setEndYear(education.getEndYear());
                    educationDTO.setMedium(education.getMedium());
                    educationDTO.setSchoolName(education.getSchoolName());
                    educationDTO.setStatus(education.getStatus());
                    educationDTO.setProofPhoto(education.getProofPhoto());
                    educationDTO.setPersonId(updatedUser.get().getId());
                    educationDTO.setCreatedBy(education.getCreatedBy().getId());
                    educationDTO.setUpdatedBy(education.getUpdatedBy().getId());
                    educationDTO.setStatus(education.getStatus());
                    educationDTO.setIsDeleted(String.valueOf(education.getIsDeleted()));
                    educationDTO.setMobileLocalId(education.getMobileLocalId());
                    educationDTOList.add(educationDTO);
                }
            }

        }
        HashMap<String, Object> userRet = new HashMap<>();
        userRet.put(GlobalConstants.BASIC_DETAIL, getPersonDetail);
        userRet.put(GlobalConstants.ADDRESS, addressDetailList);
        userRet.put(GlobalConstants.EDUCATION, educationDTOList);
        return new ResponseEntity(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userRet), HttpStatus.CREATED);
    }

    private PersonEducation updateEducation(EducationDTO educationDTO, User user, Admin createdBy, Admin updatedBy) {


        PersonEducation personEducation = new PersonEducation();
        if (educationRepository.existsById(educationDTO.getPersonEducationId())) {
            personEducation.setPersonEducationId(educationDTO.getPersonEducationId());
        }

        personEducation.setPerson(user);
        personEducation.setDegreeId(educationDTO.getDegreeId());
        personEducation.setEndYear(educationDTO.getEndYear());
        personEducation.setStartYear(educationDTO.getStartYear());
        personEducation.setMedium(educationDTO.getMedium());
        personEducation.setProofPhoto(educationDTO.getProofPhoto());
        personEducation.setResult(educationDTO.getResult());
        personEducation.setSchoolName(educationDTO.getSchoolName());
        personEducation.setMobileLocalId(educationDTO.getMobileLocalId());
        personEducation.setStatus(educationDTO.getStatus());
        personEducation.setCreatedBy(createdBy);
        personEducation.setUpdatedBy(updatedBy);
        PersonEducation personEducation1 = educationRepository.save(personEducation);
        personEducation1.setMobileLocalId(personEducation.getMobileLocalId());
        return personEducation1;

    }

    private Address updatePersonAddress(UpdateAddressFromUserDTO updateUserDTO, User personId, Admin createdBy, Admin updatedBy) {

        Address address = new Address();
        if (addressRepository.existsById(updateUserDTO.getAddressId())) {
            address.setId(updateUserDTO.getAddressId());
        }
        address.setPersonId(personId);
        address.setAddressType(updateUserDTO.getAddressType());
        address.setAddressText(updateUserDTO.getAddressText());
        address.setCountry(coutryRepository.findById(updateUserDTO.getCountryId()).get());
        address.setDistrict(districtRepository.findById(updateUserDTO.getDistrictId()).get());
        address.setState(stateRepository.findById(updateUserDTO.getStateId()).get());
        address.setMobileLocalId(updateUserDTO.getMobileLocalId());
        address.setCreatedBy(createdBy);
        address.setUpdatedBy(updatedBy);
        address.setStatus(updateUserDTO.getStatus());
        address.setMobileLocalId(updateUserDTO.getMobileLocalId());
        address.setIsDeleted(updateUserDTO.getIsDeleted());
        Address address1 =  addressRepository.save(address);
        address1.setMobileLocalId(updateUserDTO.getMobileLocalId());
        return address1;
    }

    private String updatePersonDetail(UpdateUserDTO updateUserDTO) {

        UpdatePersonDetailDTO personDetail = updateUserDTO.getPersonDetail();
        User person = new User();

        Optional<Admin> admin = adminRepository.findById(personDetail.getAdminId());
        if (!admin.isPresent()) {
            return "Can not found Admin";
        }

        Optional<Village> village = villageRepository.findById(personDetail.getVillageId());
        if (!village.isPresent()) {
            return "Can not found Village";
        }
        Optional<Village> husbandvillage = null;
        if (personDetail.getHusbandVillageId() != null && (!personDetail.getHusbandVillageId().isEmpty())) {
            husbandvillage = villageRepository.findById(personDetail.getVillageId());
            if (!husbandvillage.isPresent()) {
                return "Can not found  husband Village";
            }
        }

        Optional<Admin> userCreated = adminRepository.findById(personDetail.getCreatedBy());
        if (!userCreated.isPresent()) {
            return "Invalid User Id in Created By";
        }

        Optional<Admin> userUpdated = adminRepository.findById(personDetail.getUpdatedBy());
        if (!userCreated.isPresent()) {
            return "Invalid User Id in Updated By";
        }

        Optional<Role> userRole = roleRepository.findByNameContainingIgnoreCase(GlobalConstants.ROLE_NORMAL);
        Set<Role> userRoleSet = new HashSet<Role>();
        userRoleSet.add(userRole.get());
        person.setRoles(userRoleSet);

        person.setId(personDetail.getPersonId());
        person.setAdmin(admin.get());
        person.setPassword(personDetail.getPassword());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        person.setBirthDate(LocalDate.parse(personDetail.getBirthDate(), df));
        person.setEmail(personDetail.getEmail());
        person.setFirstName(personDetail.getFirstName());
        person.setGender(personDetail.getGender());
        person.setLastName(personDetail.getLastName());
        person.setMaritalStatus(personDetail.getMaritualStatus());
        person.setMobileno(personDetail.getMobileno());
        person.setBloodGroup(personDetail.getBloodGroup());
        person.setProfilePic(personDetail.getProfilePic());
        person.setSurname(personDetail.getSurname());
        person.setHusbandFirstName(personDetail.getHusbandFirstName());
        person.setHusbandLastName(personDetail.getHusbandLastName());
        person.setHusbandSurname(personDetail.getHusbandSurname());
        if (husbandvillage != null) {
//            person.setHusbandVillage(husbandvillage.get());
        }
        person.setVillage(village.get());
        person.setMobileLocalId(personDetail.getMobileLocalId());

        person.setCreatedBy(userCreated.get());
        person.setUpdatedBy(userUpdated.get());
        person.setStatus(personDetail.getStatus());
        person.setIsDeleted(personDetail.getIsDeleted());

        User userRes = userRepository.saveAndFlush(person);

        return "";
    }

    @PutMapping("/updateAdmin")
    public ResponseEntity<?> updateAdmin(@Valid @RequestBody UpdateAdmin updateAdmin) {
        Optional<Admin> admin = adminRepository.findById(updateAdmin.getAdminId());
        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find admin by admin id", null),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findById(updateAdmin.getPersonId());
        if (!user.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }
        Admin adminNew = admin.get();
        adminNew.setPerson(user.get());

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "SUCCESS", adminRepository.save(adminNew)),
                HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/createAdmin")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest) {
        Admin result = null;
        if (!userRepository.existsById(addAdminRequest.getPersonId())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<AdminRole> adminRole = adminRoleRepository.findById(addAdminRequest.getRoleId());
        if (!adminRole.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Role Not Found", result), HttpStatus.OK);
        }

        List<Admin> admins = adminRepository.isExistsAdminByPerson(addAdminRequest.getPersonId());
        if (Objects.nonNull(admins) && admins.stream().anyMatch(admin -> admin.getAdminRole().getId().equalsIgnoreCase(adminRole.get().getId()))) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Admin exist with person id and role id", result), HttpStatus.OK);
        }
        Admin newAdmin = new Admin();
        Optional<User> person = userRepository.findById(addAdminRequest.getPersonId());
        if (!person.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }
        newAdmin.setName(person.get().getFirstName() + " " + person.get().getLastName());
        newAdmin.setPerson(person.get());
        newAdmin.setAdminRole(adminRole.get());
        result = adminRepository.save(newAdmin);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", result), HttpStatus.OK);

    }

    @GetMapping("/admin/")
    public ResponseEntity<?> getAllAdmin() {
        List<Admin> admins = adminRepository.findAll();
        List<AdminBasicDetail> BasicDetails = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();
        for (Admin admin : admins) {
            AdminBasicDetail adminBasicDetail = new AdminBasicDetail();
            adminBasicDetail.setAdminId(admin.getId());
            if (admin.getPerson() != null) {
                if (admin.getPerson().getId() != null) {
                    adminBasicDetail.setPersonId(admin.getPerson().getId());
                }
                if (admin.getPerson().getFirstName() != null) {
                    adminBasicDetail.setFirstName(admin.getPerson().getFirstName());
                }
                if (admin.getPerson().getLastName() != null) {
                    adminBasicDetail.setLastName(admin.getPerson().getLastName());
                }
                if (admin.getPerson().getEmail() != null) {
                    adminBasicDetail.setEmail(admin.getPerson().getEmail());
                }
                if (admin.getPerson().getGender() != null) {
                    adminBasicDetail.setGender(admin.getPerson().getGender());
                }
                if (admin.getPerson().getMobileno() != null) {
                    adminBasicDetail.setMobileno(admin.getPerson().getMobileno());
                }
                if (admin.getPerson().getProfilePic() != null) {
                    adminBasicDetail.setProfilePic(admin.getPerson().getProfilePic());
                }
                if (admin.getPerson().getSurname() != null) {
                    adminBasicDetail.setSurname(admin.getPerson().getSurname());
                }
                if (admin.getPerson().getHusbandFirstName() != null) {
                    adminBasicDetail.setHusbandFirstName(admin.getPerson().getHusbandFirstName());
                }
                if (admin.getPerson().getHusbandLastName() != null) {
                    adminBasicDetail.setHusbandLastName(admin.getPerson().getHusbandLastName());
                }
                if (admin.getPerson().getHusbandLastName() != null) {
                    adminBasicDetail.setHusbandLastName(admin.getPerson().getHusbandLastName());
                }
                /*if (admin.getPerson().getHusbandVillage() != null && admin.getPerson().getHusbandVillage().getName() != null) {
                    adminBasicDetail.setHusbandVillageName(admin.getPerson().getHusbandVillage().getName());
                }*/
                if (admin.getPerson().getVillage() != null && admin.getPerson().getVillage().getName() != null) {
                    adminBasicDetail.setVillageName(admin.getPerson().getVillage().getName());
                }
                if (admin.getAdminRole() != null && admin.getAdminRole().getId() != null) {
                    adminBasicDetail.setAdminType(admin.getAdminRole().getName());
                }
            }

            BasicDetails.add(adminBasicDetail);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", BasicDetails), HttpStatus.OK);
    }

    @GetMapping("/byadmin/{adminId}")
    public ResponseEntity<?> getpersonsByAdmin(@PathVariable String adminId) {
        try {
            if (adminRepository.existsById(adminId)) {
                List<User> users = userRepository.findAllByAdminId(adminId);
                HashMap<String, Object> retObj = new HashMap<>();
                List<PersonalDetail> personalDetails = new ArrayList<>();
                List<AddressDetail> addressDetails = new ArrayList<>();
                List<EducationDTO> educationDetails = new ArrayList<>();
                users.forEach((user) -> {
                    PersonalDetail personalDetail = new PersonalDetail();
                    personalDetail.setBirthDate(user.getBirthDate().toString());
                    personalDetail.setBloodGroup(user.getBloodGroup());
                    personalDetail.setContactNo(user.getMobileno());
                    personalDetail.setEmail(user.getEmail());
                    personalDetail.setFirstName(user.getFirstName());
                    personalDetail.setGender(user.getGender());
                    personalDetail.setLastName(user.getLastName());
                    personalDetail.setMaritalStatus(user.getMaritalStatus());
                    personalDetail.setCreatedAt(user.getMobileno());
                    personalDetail.setPersonId(user.getId());

                    //todo : remove temp string in setProfilePic
                    personalDetail.setProfilePic("");
                    personalDetail.setSurname(user.getSurname());
                    personalDetail.setVillageId(user.getVillageId());

                    personalDetail.setHusbandVillageId(user.getHusbandVillageId() != null ? user.getHusbandVillageId() : "");
                    personalDetail.setHusbandFirstName(user.getHusbandFirstName() != null ? user.getHusbandFirstName() : "");
                    personalDetail.setHusbandLastName(user.getHusbandLastName() != null ? user.getHusbandLastName() : "");
                    personalDetail.setHusbandSurname(user.getHusbandSurname() != null ? user.getHusbandSurname() : "");

                    personalDetail.setIsSync(GlobalConstants.SYNC_STATUS);

                    personalDetail.setIsDelete(String.valueOf(user.getIsDeleted()));
                    personalDetail.setDeletedBy(String.valueOf(user.getDeletedBy() != null ? user.getDeletedBy().getId() : ""));
                    personalDetail.setDeletedAt(String.valueOf(user.getDeletedAt()));
                    personalDetail.setStatus(String.valueOf(user.getStatus()));
                    personalDetail.setUpdatedAt(String.valueOf(user.getUpdatedAt().toString()));
                    personalDetail.setUpdatedBy(String.valueOf(user.getUpdatedBy() != null ? user.getUpdatedBy().getId() : ""));
                    personalDetail.setCreatedBy(String.valueOf(user.getCreatedBy() != null ? user.getCreatedBy().getId() : ""));
                    personalDetail.setCreatedAt(String.valueOf(user.getCreatedAt().toString()));
                    personalDetails.add(personalDetail);


                    List<Address> address = user.getAddressList();
                    address.forEach((add) -> {
                        AddressDetail addressDetail = new AddressDetail();
                        addressDetail.setPersonAddressId(add.getId());
                        addressDetail.setAddressText(add.getAddressText());
                        addressDetail.setType(add.getAddressType());
                        addressDetail.setCountryId(add.getCountryId());
                        addressDetail.setDistrictId(add.getDistrictId());
                        addressDetail.setStateId(add.getStateId());
                        addressDetail.setPersonId(add.getPersonId().getId());

                        addressDetail.setIsDelete(String.valueOf(add.getIsDeleted()));

                        addressDetail.setUpdatedAt(String.valueOf(add.getUpdatedAt().toString()));
                        addressDetail.setUpdatedBy(String.valueOf(add.getUpdatedBy().getId()));

                        addressDetail.setCreatedBy(String.valueOf(add.getCreatedBy() != null ? add.getCreatedBy().getId() : null));
                        addressDetail.setCreatedAt(String.valueOf(add.getCreatedAt().toString()));
                        addressDetail.setDeletedBy(String.valueOf(add.getDeletedBy() != null ? add.getDeletedBy().getId() : ""));
                        addressDetail.setDeletedAt(String.valueOf(add.getDeletedAt() != null ? add.getDeletedAt() : ""));


                        addressDetail.setIsSync(GlobalConstants.SYNC_STATUS);
                        addressDetails.add(addressDetail);
                    });

                    List<PersonEducation> personEducationList = educationRepository.findAllByPersonId(user.getId());

                    if (personEducationList.isEmpty()) {

                    } else {
                        personEducationList.forEach((education) -> {
                            EducationDTO educationDTO = new EducationDTO(
                                    education.getPersonEducationId(),
                                    education.getPerson().getId(),
                                    education.getDegreeId(),
                                    education.getSchoolName(),
                                    education.getResult() != null ? education.getResult() : "",
                                    education.getStartYear(),
                                    education.getEndYear(),
                                    education.getProofPhoto() != null ? education.getProofPhoto() : "",
                                    education.getMedium(),
                                    education.getCreatedBy() != null ? education.getCreatedBy().getId() : "",
                                    education.getUpdatedBy() != null ? education.getUpdatedBy().getId() : "",
                                    education.getDeletedBy() != null ? education.getDeletedBy().getId() : "",
                                    education.getCreatedAt() != null ? education.getCreatedAt().toString(): "",
                                    education.getUpdatedAt() != null ? education.getUpdatedAt().toString(): "",
                                    education.getDeletedAt() != null ? education.getDeletedAt().toString(): "",
                                    String.valueOf(education.getIsDeleted()),
                                    education.getMobileLocalId(),
                                    education.getStatus(), GlobalConstants.SYNC_STATUS
                            );
                            educationDetails.add(educationDTO);
                                }
                        );
                    }
                });

                retObj.put("personalDetail", personalDetails);
                retObj.put("address", addressDetails);
                retObj.put("education",educationDetails);
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Success", retObj), HttpStatus.OK);

            } else {
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Can not found admin", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Something went wrong", e), HttpStatus.OK);
        }
    }

    @GetMapping("/getAdminsByType/{adminType}")
    public ResponseEntity<?> getAdminsByType(@PathVariable String adminType) {
        AdminRole adminRole = adminRoleRepository.findByName(adminType);
        if (Objects.isNull(adminRole)) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found admin type", null), HttpStatus.BAD_REQUEST);
        }

        List<Admin> admins = adminRepository.AdminByRoles(adminType);
        List<AuthAdmin> authAdminList = new ArrayList<>();
        admins.forEach((admin) -> {
            AuthAdmin authAdminData = new AuthAdmin();
            authAdminData.setAdminId(admin.getId());
            authAdminData.setAdminName(admin.getName());
            authAdminData.setPersonId(admin.getPerson().getId());
            authAdminData.setAdminType(adminType);
            authAdminList.add(authAdminData);
        });
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Success", authAdminList), HttpStatus.OK);
    }
}
