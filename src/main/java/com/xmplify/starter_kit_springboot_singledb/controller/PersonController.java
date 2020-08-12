package com.xmplify.starter_kit_springboot_singledb.controller;

import com.google.common.base.Strings;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.admin.AdminBasicDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.FilterUserDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonBasicDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonListDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.FamilyCode;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.SettingConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.security.SecurityUtils;
import com.xmplify.starter_kit_springboot_singledb.service.FileService;
import com.xmplify.starter_kit_springboot_singledb.service.RoleService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final
    PersonSettingsRepository settingsRepository;
    private final
    AdminRepository adminRepository;
    private final
    AdminRoleRepository adminRoleRepository;
    private final
    VillageRepository villageRepository;
    private final
    CoutryRepository coutryRepository;
    private final
    DistrictRepository districtRepository;
    private final
    StateRepository stateRepository;
    private final
    AddressRepository addressRepository;
    private final
    UserMapper userMapper;
    private final
    UserService userService;
    private final
    Validators validators;
    private final
    EducationRepository educationRepository;
    private final
    RoleService roleService;
    private final
    FileService fileService;
    private final
    FamilyCode familyCodeService;

    @Autowired
    public PersonController(AdminRoleRepository adminRoleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, RoleRepository roleRepository, PersonSettingsRepository settingsRepository, AdminRepository adminRepository, VillageRepository villageRepository, Validators validators, EducationRepository educationRepository, UserMapper userMapper, CoutryRepository coutryRepository, DistrictRepository districtRepository, FileService fileService, StateRepository stateRepository, FamilyCode familyCodeService, UserService userService, AddressRepository addressRepository) {
        this.adminRoleRepository = adminRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.settingsRepository = settingsRepository;
        this.adminRepository = adminRepository;
        this.villageRepository = villageRepository;
        this.validators = validators;
        this.educationRepository = educationRepository;
        this.userMapper = userMapper;
        this.coutryRepository = coutryRepository;
        this.districtRepository = districtRepository;
        this.fileService = fileService;
        this.stateRepository = stateRepository;
        this.familyCodeService = familyCodeService;
        this.userService = userService;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/")
    public ResponseEntity<?> listUser(@PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        List<PersonListDTO> personListDTOS = PersonListDTO.create(users.getContent());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", personListDTOS), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUser(FilterUserDTO filterUser, @PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<User> users = userService.findAll(filterUser, pageable);
        List<PersonListDTO> personListDTOS = PersonListDTO.create(users.getContent());
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", personListDTOS), HttpStatus.OK);
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
        adminRepository.save(adminNew);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "SUCCESS", null),
                HttpStatus.OK);
    }


    @PostMapping("/admin/create")
    @Transactional
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
        adminRepository.save(newAdmin);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", null), HttpStatus.OK);
    }

    @GetMapping("/admin/")
    public ResponseEntity<?> getAllAdmin() {
        List<Admin> admins = userService.findAllAdmin();
        List<AdminBasicDetailDTO> basicDetails = AdminBasicDetailDTO.create(admins);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", basicDetails), HttpStatus.OK);
    }

    @GetMapping("/byadmin/{adminId}")
        public ResponseEntity<?> getPersonsByAdmin(@PathVariable String adminId) {
        if (!validators.isExistAdmin(adminId)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Can not found admin", null), HttpStatus.OK);
        }
        List<User> users = userService.findAllUserByAdminId(adminId);
        List<PersonListDTO> personListDTOS = PersonListDTO.create(users);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", personListDTOS), HttpStatus.OK);
    }

    @GetMapping("/admin/type/{adminType}")
    public ResponseEntity<?> getAdminsByType(@PathVariable String adminType) {
        if (!adminRoleRepository.existsByName(adminType)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found admin type", null), HttpStatus.BAD_REQUEST);
        }
        List<Admin> admins = adminRepository.AdminByRoles(adminType);
        List<AdminBasicDetailDTO> basicDetails = AdminBasicDetailDTO.create(admins);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", basicDetails), HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable String personId) {
        if (Objects.nonNull(personId) && !StringUtils.isEmpty(personId)) {
            if (userRepository.existsById(personId)) {
                return userService.getPersonDetail(personId);
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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Invalid setting type", null), HttpStatus.OK);
        }
        Optional<PersonSetting> setting = settingsRepository.findById(settingDTO.getId());
        if (!setting.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Invalid Setting Id", null), HttpStatus.OK);
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
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", settingsRepository.save(personSetting)), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Transactional
    public ResponseEntity<?> addPerson(@Valid @ModelAttribute PersonBasicDetailDTO personBasicDetailDTO, HttpServletRequest request) {
        Optional<User> oldUser = Optional.empty();
        if (Objects.nonNull(personBasicDetailDTO) && Objects.nonNull(personBasicDetailDTO.getPersonDetail()) && Objects.nonNull(personBasicDetailDTO.getPersonDetail().getId())) {
            List<String> messages = validators.validateUpdatePersonBasicDetailDTO(personBasicDetailDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
            }
            oldUser = userRepository.findById(personBasicDetailDTO.getPersonDetail().getId());
        } else {
            List<String> messages = validators.validateAddPersonBasicDetailDTO(personBasicDetailDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
            }
        }
        PersonalDetailDTO personalDetailDTO = personBasicDetailDTO.getPersonDetail();
        List<AddressDTO> addressDTO = personBasicDetailDTO.getAddresses();
        MultipartFile multipartFile = personalDetailDTO.getProfilePic();
        if (Objects.nonNull(multipartFile)) {
            if (oldUser.isPresent() && !Strings.isNullOrEmpty(oldUser.get().getProfilePic())) {
                if (!fileService.deleteFile(oldUser.get().getProfilePic(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT, request.getServletContext())) {
                    return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Problem to delete old Image. see logs", null), HttpStatus.OK);
                }
                personalDetailDTO.setProfileURL(null);
            }
            String fileStorePath = fileService.uploadFile(multipartFile, request.getServletContext(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT);
            if (Objects.isNull(fileStorePath)) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Problem to store Image see more logs", null), HttpStatus.OK);
            }
            personalDetailDTO.setProfileURL(fileStorePath);
        }else {
            if (oldUser.isPresent() && !Strings.isNullOrEmpty(oldUser.get().getProfilePic())) {
                personalDetailDTO.setProfileURL(oldUser.get().getProfilePic());
            }
        }
        if (oldUser.isPresent()) {
            return userService.updatePerson(personalDetailDTO, addressDTO, oldUser.get());
        } else {
            return userService.addPerson(personalDetailDTO, addressDTO);
        }
    }

    @GetMapping("/getFamilyCode/{villageId}")
    @Synchronized
    public ResponseEntity<?> getFamilyCode(@PathVariable String villageId) {
        if (!validators.validateVillageId(villageId)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "InValid VillageId", null), HttpStatus.OK);
        }
        Village village = villageRepository.findById(villageId).get();
        String currentFamilyCode = village.getShortForm() + GlobalConstants.UNDER_SCORE + familyCodeService.getValue();
        if (!userRepository.existsByfamilyCode(currentFamilyCode)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Family code will be expire if SomeOne Used", currentFamilyCode), HttpStatus.OK);
        } else {
            familyCodeService.increment();
            familyCodeService.getValue();
            String newFamilyCode = village.getShortForm() + GlobalConstants.UNDER_SCORE + familyCodeService.getValue();
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), true, "Family code will be expire if SomeOne Used", newFamilyCode), HttpStatus.OK);
        }
    }

    @PostMapping("/updatePersonAdmin")
    public ResponseEntity<?> updatePersonAdmin(@RequestBody UpdatePersonAdminDTO updatePersonAdmin) {
        if (Objects.nonNull(updatePersonAdmin) && Objects.nonNull(updatePersonAdmin.getAdminId())
                && Objects.nonNull(updatePersonAdmin.getPersonId())
                && !StringUtils.isEmpty(updatePersonAdmin.getAdminId())
                && !StringUtils.isEmpty(updatePersonAdmin.getPersonId())
        ) {
            if (!userRepository.existsById(updatePersonAdmin.getPersonId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), true, "person Not Found", null), HttpStatus.NOT_FOUND);
            } else {
                userRepository.updateAdmin(updatePersonAdmin.getAdminId(), updatePersonAdmin.getPersonId());
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", null), HttpStatus.OK);
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
            if (!userRepository.existsById(SecurityUtils.getCurrentUserId())) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "person Not Found", null), HttpStatus.NOT_FOUND);
            } else {
                userRepository.changePassword(SecurityUtils.getCurrentUserId(), passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", null), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "person Id and admin id can not be null or empty", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/family/{personId}")
    public ResponseEntity<?> getAllFamilyMember(@PathVariable String personId){
        List<String> messages = new ArrayList<>();
        validators.validateUserId(personId,messages);
        if (!messages.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid Requst Reason : ", messages), HttpStatus.BAD_REQUEST);
        }

        List<PersonListDTO> familyList = userService.getFamilyList(personId);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", familyList), HttpStatus.OK);
    }
}
