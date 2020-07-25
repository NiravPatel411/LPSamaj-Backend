package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.PersonBasicDetailDTO1;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonBasicDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail;
import com.xmplify.starter_kit_springboot_singledb.constants.FamilyCode;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.SettingConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDBDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdatePersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateUserDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.service.FileService;
import com.xmplify.starter_kit_springboot_singledb.service.RoleService;
import com.xmplify.starter_kit_springboot_singledb.security.SecurityUtils;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import com.xmplify.starter_kit_springboot_singledb.service.Validators;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    PersonSettingsRepository settingsRepository;

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

    @Autowired
    RoleService roleService;

    @Autowired
    FileService fileService;

    @Autowired
    FamilyCode familyCodeService;

    @GetMapping("/")
    public ResponseEntity<?> listUser(@PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        List<PersonBasicDetailDTO1> personBasicDetailDTOS = userMapper.toPersonBasicDetailDTO(users.getContent());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personBasicDetailDTOS), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUser(FilterUserDTO filterUser, @PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<User> users = userService.findAll(filterUser, pageable);
        List<PersonBasicDetailDTO1> personBasicDetailDTOS = userMapper.toPersonBasicDetailDTO(users.getContent());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", personBasicDetailDTOS), HttpStatus.OK);
    }

//    @PostMapping(value = "/add")
//    public ResponseEntity<?> registerUser(@Valid @ModelAttribute UpdateUserDTO updateUserDTO, BindingResult result, HttpServletRequest request) {
//
//        if (result.hasErrors()) {
//            List<String> errors = new ArrayList<>();
//            getError(result, errors);
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
//        }
//        if (updateUserDTO.getPersonDetail().getPersonId() != null && userRepository.existsById(updateUserDTO.getPersonDetail().getPersonId())) {
//            ResponseEntity<?> responseEntity = this.updateUser(updateUserDTO, result, request);
//            return responseEntity;
//        } else {
//            AddPersonDTO addPersonDTO = userMapper.updateUserDTOToAddUserDTO(updateUserDTO);
//            List<String> messages = validators.validateAddPersonDTO(addPersonDTO);
//            if (!messages.isEmpty()) {
//                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, messages.toString(), null),
//                        HttpStatus.BAD_REQUEST);
//            }
//            User savedUser = userService.save(addPersonDTO, request);
//            return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userMapper.userToAddPersonResponse(savedUser, addPersonDTO)), HttpStatus.CREATED);
//
//        }
//    }

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
    public ResponseEntity<?> updateUser(@Valid @ModelAttribute UpdateUserDTO updateUserDTO, BindingResult result, HttpServletRequest request) {
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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }

        if (!userRepository.existsById(updateUserDTO.getPersonDetail().getPersonId())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "can not found person", null), HttpStatus.BAD_REQUEST);
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
            String resp = updatePersonDetail(updateUserDTO, request);
            if (!StringUtils.isEmpty(resp)) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, resp, null), HttpStatus.BAD_REQUEST);
            }
        }

        Optional<User> user = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "some thing went wrong", null), HttpStatus.BAD_REQUEST);
        }

        List<Address> returnAddress = new ArrayList<>();
        for (UpdateAddressFromUserDTO address : updateUserDTO.getAddress()) {
            if (address.isSync()) {
                Optional<Admin> createdBy = adminRepository.findById(address.getCreatedBy());
                if (!createdBy.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "createdBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }

                Optional<Admin> updatedBy = adminRepository.findById(address.getUpdatedBy());
                if (!updatedBy.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "updatedBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }
                returnAddress.add(updatePersonAddress(address, user.get(), createdBy.get(), updatedBy.get()));
            }
        }
        List<PersonEducation> educationList = new ArrayList<>();

        for (EducationDBDTO education : updateUserDTO.getEducation()) {
            if (education.getIsSync().equalsIgnoreCase("true")) {
                Optional<Admin> createdBy = adminRepository.findById(education.getCreatedBy());
                if (!createdBy.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "createdBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }

                Optional<Admin> updatedBy = adminRepository.findById(education.getUpdatedBy());
                if (!updatedBy.isPresent()) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "updatedBy does not exist in User", null), HttpStatus.BAD_REQUEST);
                }
                educationList.add(updateEducation(education, user.get(), createdBy.get(), updatedBy.get()));
            }
        }

        userRepository.flush();
        Optional<User> updatedUser = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId());
        GetPersonDetail getPersonDetail = new GetPersonDetail();
        List<GetAddressDetail> addressDetailList = new ArrayList<>();
        List<EducationDBDTO> educationDTOList = new ArrayList<>();
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
                    EducationDBDTO educationDTO = new EducationDBDTO();
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
        return new ResponseEntity<>(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userRet), HttpStatus.CREATED);
    }

    private PersonEducation updateEducation(EducationDBDTO educationDTO, User user, Admin createdBy, Admin updatedBy) {

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
        Address address1 = addressRepository.save(address);
        address1.setMobileLocalId(updateUserDTO.getMobileLocalId());
        return address1;
    }

    private String updatePersonDetail(UpdateUserDTO updateUserDTO, HttpServletRequest request) {

        UpdatePersonDetailDTO personDetail = updateUserDTO.getPersonDetail();
        User oldUser = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId()).get();
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
        person.setPassword(passwordEncoder.encode(personDetail.getPassword()));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        person.setBirthDate(LocalDate.parse(personDetail.getBirthDate(), df));
        person.setEmail(personDetail.getEmail());
        person.setFirstName(personDetail.getFirstName());
        person.setGender(personDetail.getGender());
        person.setLastName(personDetail.getLastName());
        person.setMaritalStatus(personDetail.getMaritualStatus());
        person.setMobileno(personDetail.getMobileno());
        person.setBloodGroup(personDetail.getBloodGroup());
        person.setSurname(personDetail.getSurname());
        person.setHusbandFirstName(personDetail.getHusbandFirstName());
        person.setHusbandLastName(personDetail.getHusbandLastName());
        person.setHusbandSurname(personDetail.getHusbandSurname());
        if (husbandvillage != null) {
//            person.setHusbandVillage(husbandvillage.get());
        }
        person.setVillage(village.get());

        person.setCreatedBy(userCreated.get());
        person.setUpdatedBy(userUpdated.get());
        person.setStatus(personDetail.getStatus());
        person.setIsDeleted(personDetail.getIsDeleted());

        if (Objects.nonNull(personDetail.getProfilePic())) {

            if (Objects.nonNull(oldUser.getProfilePic()) && !StringUtils.isEmpty(oldUser.getProfilePic())) {
                ServletContext context = request.getServletContext();
                String fullPath = context.getRealPath(GlobalConstants.UPLOAD_DIR + GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH + GlobalConstants.PROFILE_EVENT + GlobalConstants.BACK_SLASH);
                File file = new File(fullPath + oldUser.getProfilePic());
                file.delete();
            }
            try {
                ServletContext context = request.getServletContext();
                String fullPath = context.getRealPath(GlobalConstants.UPLOAD_DIR + GlobalConstants.IMAGE + GlobalConstants.BACK_SLASH + GlobalConstants.PROFILE_EVENT + GlobalConstants.BACK_SLASH);
                MultipartFile file = personDetail.getProfilePic();
                if (!file.isEmpty()) {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(fullPath);
                    if (!Files.exists(path)) {
                        Files.createDirectories(path);
                    }
                    String storePath = UUID.randomUUID() + "." + file.getOriginalFilename().split("\\.(?=[^\\.]+$)")[1];
                    Path filePath = Paths.get(path + GlobalConstants.BACK_SLASH + storePath);
                    Files.write(filePath, bytes);
                    person.setProfilePic(storePath);
                }
            } catch (Exception e) {
                //log
            }
        }

        User userRes = userRepository.saveAndFlush(person);

        return "";
    }

    @PutMapping("/updateAdmin")
    public ResponseEntity<?> updateAdmin(@Valid @RequestBody UpdateAdmin updateAdmin) {
        Optional<Admin> admin = adminRepository.findById(updateAdmin.getAdminId());
        if (!admin.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find admin by admin id", null),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = userRepository.findById(updateAdmin.getPersonId());
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }
        Admin adminNew = admin.get();
        adminNew.setPerson(user.get());

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "SUCCESS", adminRepository.save(adminNew)),
                HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/createAdmin")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest) {
        Admin result = null;
        if (!userRepository.existsById(addAdminRequest.getPersonId())) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<AdminRole> adminRole = adminRoleRepository.findById(addAdminRequest.getRoleId());
        if (!adminRole.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Role Not Found", result), HttpStatus.OK);
        }

        List<Admin> admins = adminRepository.isExistsAdminByPerson(addAdminRequest.getPersonId());
        if (Objects.nonNull(admins) && admins.stream().anyMatch(admin -> admin.getAdminRole().getId().equalsIgnoreCase(adminRole.get().getId()))) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Admin exist with person id and role id", result), HttpStatus.OK);
        }
        Admin newAdmin = new Admin();
        Optional<User> person = userRepository.findById(addAdminRequest.getPersonId());
        if (!person.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not find person by person id", null),
                    HttpStatus.BAD_REQUEST);
        }
        newAdmin.setName(person.get().getFirstName() + " " + person.get().getLastName());
        newAdmin.setPerson(person.get());
        newAdmin.setAdminRole(adminRole.get());
        result = adminRepository.save(newAdmin);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", result), HttpStatus.OK);

    }

    @GetMapping("/admin/")
    public ResponseEntity<?> getAllAdmin() {
        List<Admin> admins = userService.findAllAdmin();
        List<AdminBasicDetail> BasicDetails = userMapper.adminToAdminBasicDetail(admins);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", BasicDetails), HttpStatus.OK);
    }

    @GetMapping("/byadmin/{adminId}")
    public ResponseEntity<?> getpersonsByAdmin(@PathVariable String adminId) {
        if (!validators.isExistAdmin(adminId)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Can not found admin", null), HttpStatus.OK);
        }
        List<User> users = userService.findAllUserByAdminId(adminId);
        List<PersonBasicDetailDTO1> personBasicDetailDTOS = userMapper.toPersonBasicDetailDTO(users);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", personBasicDetailDTOS), HttpStatus.OK);
    }

    @GetMapping("/getAdminsByType/{adminType}")
    public ResponseEntity<?> getAdminsByType(@PathVariable String adminType) {
        AdminRole adminRole = adminRoleRepository.findByName(adminType);
        if (Objects.isNull(adminRole)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found admin type", null), HttpStatus.BAD_REQUEST);
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
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", authAdminList), HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable String personId) {
        if (Objects.nonNull(personId) && !StringUtils.isEmpty(personId)) {
            if (userRepository.existsById(personId)) {
                PersonAllDetails personAllDetails = userService.getPersonAllServiceByPersonId(personId);
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", personAllDetails), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "person Not Found", null), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "person Id can not be null or empty", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateUserSetting")
    public ResponseEntity<?> updateUserSetting(@RequestBody PersonSettingDTO settingDTO) {

        if (settingDTO.getType() == null || settingDTO.getType().isEmpty()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Invalid setting type", null), HttpStatus.OK);
        }

        Optional<PersonSetting> setting = settingsRepository.findById(settingDTO.getId());
        if (!setting.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Invalid Setting Id", null), HttpStatus.OK);
        }

        PersonSetting personSetting = setting.get();

        String type = settingDTO.getType();

        switch (type) {
            case SettingConstants.BLOOD_DONATE:
                personSetting.setBloodDonate(settingDTO.getValue());
                break;
            case SettingConstants.ADMIN_CAN_UPDATE:
                personSetting.setAdminCanUpdate(settingDTO.getValue());
                break;
            case SettingConstants.NEWS_NOTIFICATION:
                personSetting.setNewsNotification(settingDTO.getValue());
                break;
            case SettingConstants.CONTACT_NUMBER_VISIBILITY:
                personSetting.setContactNumberVisibility(settingDTO.getValue());
                break;
            case SettingConstants.PROFILE_PICTURE_VISIBLITY:
                personSetting.setProfilePictureVisiblity(settingDTO.getValue());
                break;
        }

        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", settingsRepository.save(personSetting)), HttpStatus.OK);
    }

    @GetMapping("/getBloodDonor")
    public ResponseEntity<?> getBloodDonor(FilterBloodGroupDTO filterUser, @PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {/*
        //Page<User> users = userRepository.findAll(textInAllColumns(filterUser.getSearchText(), filterUser.getfields()), pageable);
        Page<User> users = userRepository.findAll(CreateSpecification(filterUser), pageable);
        List<PersonBasicDetailDTO1> listPersonBasicDetails = new ArrayList<>();
        users.forEach(user -> {
            PersonBasicDetailDTO1 listPersonBasicDetail = new PersonBasicDetailDTO1();

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

        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", listPersonBasicDetails), HttpStatus.OK);
    */
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "nnnn", null), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Transactional
    public ResponseEntity<?> addPerson(@Valid @ModelAttribute PersonBasicDetailDTO personBasicDetailDTO, HttpServletRequest request){
        String type = "add";
        Optional<User> oldUser = Optional.empty();
        if(Objects.nonNull(personBasicDetailDTO) && Objects.nonNull(personBasicDetailDTO.getPersonDetail()) && Objects.nonNull(personBasicDetailDTO.getPersonDetail().getId())){
            List<String> messages = validators.validateUpdatePersonBasicDetailDTO(personBasicDetailDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
            }
            oldUser = userRepository.findById(personBasicDetailDTO.getPersonDetail().getId());
        } else {

            List<String> messages = validators.validateAddPersonBasicDetailDTO(personBasicDetailDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
            }
        }

        PersonalDetail personalDetail = personBasicDetailDTO.getPersonDetail();
        List<AddressDTO> addressDTO = personBasicDetailDTO.getAddresses();

        MultipartFile multipartFile = personalDetail.getProfilePic();
        if(Objects.nonNull(multipartFile)) {
            if(oldUser.isPresent()){
                if(!fileService.deleteFile(oldUser.get().getProfilePic(),GlobalConstants.IMAGE,GlobalConstants.PROFILE_EVENT,request.getServletContext())){
                    return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Problem to delete old Image. see logs", null), HttpStatus.OK);
                }
            }
            String fileStorePath = fileService.uploadFile(multipartFile, request.getServletContext(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT);
            if (Objects.isNull(fileStorePath)) {
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), false, "Problem to store Image see more logs", null), HttpStatus.OK);
            }
            personalDetail.setProfileURL(fileStorePath);
        }
        if(oldUser.isPresent()){
            return userService.updatePerson(personalDetail, addressDTO,oldUser.get());
        } else {
            return userService.addPerson(personalDetail, addressDTO);
        }
    }


    @GetMapping("/getFamilyCode/{villageId}")
    @Synchronized
    public ResponseEntity<?> getFamilyCode(@PathVariable String villageId){
        if(!validators.validateVillageId(villageId)){
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "InValid VillageId",null), HttpStatus.OK);
        }

        Village village = villageRepository.findById(villageId).get();
        String currentFamilyCode = village.getShortForm() + GlobalConstants.UNDER_SCORE + familyCodeService.getValue();
        if(userRepository.existsByfamilyCode(currentFamilyCode)){
            familyCodeService.increment();
            familyCodeService.getValue();
            String newFamilyCode = village.getShortForm() + GlobalConstants.UNDER_SCORE + familyCodeService.getValue();
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Family code will be expire if SomeOne Used",newFamilyCode), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Family code will be expire if SomeOne Used",currentFamilyCode), HttpStatus.OK);
        }


    }

    @PostMapping("/updatePersonAdmin")
    public ResponseEntity<?> updatePersonAdmin(@RequestBody UpdatePersonAdminDTO updatePersonAdmin) {

        if (Objects.nonNull(updatePersonAdmin) && Objects.nonNull(updatePersonAdmin.getAdminId())
                && Objects.nonNull(updatePersonAdmin.getPersonId())
                && !StringUtils.isEmpty(updatePersonAdmin.getAdminId())
                && !StringUtils.isEmpty(updatePersonAdmin.getPersonId())
        ) {
            if (userRepository.existsById(updatePersonAdmin.getPersonId())) {
                userRepository.updateAdmin(updatePersonAdmin.getAdminId(), updatePersonAdmin.getPersonId());
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "person Not Found", null), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "person Id and admin id can not be null or empty", null), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {

        if (Objects.nonNull(changePasswordDTO)
                && Objects.nonNull(changePasswordDTO.getNewPassword())
                && !StringUtils.isEmpty(changePasswordDTO.getNewPassword())
        ) {
            if (userRepository.existsById(SecurityUtils.getCurrentUserId())) {


                userRepository.changePassword(SecurityUtils.getCurrentUserId(), passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", null), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "person Not Found", null), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "person Id and admin id can not be null or empty", null), HttpStatus.BAD_REQUEST);
        }

    }

}
