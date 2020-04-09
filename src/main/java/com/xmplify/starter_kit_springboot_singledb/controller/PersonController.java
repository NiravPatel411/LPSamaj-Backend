package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.UserMapper;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    AddressRepository addressRepository;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public ResponseEntity<?> listUser(final Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<ListPersonBasicDetail> listPersonBasicDetails = new ArrayList<>();
        users.forEach(user -> {

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
            listPersonBasicDetail.setCreatedDate(user.getCreatedDate());
            listPersonBasicDetail.setUpdatedDate(user.getLastModifiedDate());
            listPersonBasicDetail.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
            listPersonBasicDetail.setUpdatedBy(user.getLastModifiedBy() != null ? user.getLastModifiedBy().getId() : null);
            listPersonBasicDetail.setIsDeleted(user.getIsDeleted());
            listPersonBasicDetail.setStatus(user.getStatus());
            if (user.getVillage() != null) {
                listPersonBasicDetail.setVillageName(user.getVillage().getName());
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
            listPersonBasicDetail.setCreatedDate(user.getCreatedDate());
            listPersonBasicDetail.setUpdatedDate(user.getLastModifiedDate());
            listPersonBasicDetail.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
            listPersonBasicDetail.setUpdatedBy(user.getLastModifiedBy().getId());
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
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Validation error", errors), HttpStatus.BAD_REQUEST);
        }
        List<String> addressErrors = new ArrayList<>();
        if (updateUserDTO.getPersonDetail().getPersonId() != null && userRepository.existsById(updateUserDTO.getPersonDetail().getPersonId())) {
            ResponseEntity<?> responseEntity = this.updateUser(updateUserDTO,result);
            return responseEntity;
        } else {
            AddPersonDTO addPersonDTO = userMapper.updateUserDTOToAddUserDTO(updateUserDTO);

            addPersonDTO.getAddress().forEach((address) -> {
                if (coutryRepository.findById(address.getCountryId()).isPresent()) {
                    addressErrors.add("Can not found country with " + address.getCountryId() + " Id");
                }
            });

            if (Objects.isNull(addressErrors)) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found country", addressErrors), HttpStatus.BAD_REQUEST);
            }

            List<String> districtErrors = new ArrayList<>();
            addPersonDTO.getAddress().forEach((address) -> {
                if (districtRepository.findById(address.getDistrictId()).isPresent()) {
                    districtErrors.add("Can not found District with " + address.getDistrictId() + "Id");
                }
            });

            if (Objects.isNull(districtErrors)) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found districts", districtErrors), HttpStatus.BAD_REQUEST);
            }

            Optional<Admin> admin = adminRepository.findById(addPersonDTO.getPersonDetail().getAdminId());
            if (!admin.isPresent()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found Admin", null), HttpStatus.BAD_REQUEST);
            }

            Optional<Village> village = villageRepository.findById(addPersonDTO.getPersonDetail().getVillageId());
            if (!village.isPresent()) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Can not found Village", null), HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByMobileno(addPersonDTO.getPersonDetail().getMobileno())) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Mobileno already in use!", null),
                        HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(addPersonDTO.getPersonDetail().getEmail())) {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Email already in use!", null),
                        HttpStatus.BAD_REQUEST);
            }
            Optional<Admin> userCreated = null;
            if(addPersonDTO.getPersonDetail().getCreatedBy() != null) {
                userCreated = adminRepository.findById(addPersonDTO.getPersonDetail().getCreatedBy());
                if (userCreated == null || !userCreated.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid User Id in Created By", null),
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Created By can not be null", null),
                        HttpStatus.BAD_REQUEST);
            }
            Optional<Admin> userUpdated = null;
            if(addPersonDTO.getPersonDetail().getUpdatedBy() != null) {
                userUpdated = adminRepository.findById(addPersonDTO.getPersonDetail().getUpdatedBy());
                if (userUpdated == null || !userCreated.isPresent()) {
                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Invalid User Id in Updated By", null),
                            HttpStatus.BAD_REQUEST);
                }
            } else {

            }

            User user = new User();
            Optional<Role> userRole = roleRepository.findByNameContainingIgnoreCase(GlobalConstants.ROLE_NORMAL);
            if (!userRole.isPresent()) {
                Role role = new Role();
                role.setDisplayName(GlobalConstants.ROLE_NORMAL);
                role.setName(GlobalConstants.ROLE_NORMAL);
                roleRepository.save(role);
            }
            /***************************************************************************************************************
             * todo : Put below code while adding profile pic
             MultipartFile file = addPersonDTO.getPersonDetail().getProfilePic();
             if(file.isEmpty()){
             return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "File can not be empty", null),
             HttpStatus.BAD_REQUEST);
             }

             try{
             byte[] bytes = file.getBytes();
             Path path = Paths.get(GlobalConstants.UPLOAD_IMAGE + addPersonDTO.getPersonDetail().getFirstName());
             Files.write(path,bytes);
             // byte[] bytes1 = file.getBytes();
             } catch(IOException e){
             e.printStackTrace();
             }
             ****************************************************************************************************************/
            Set<Role> userRoleSet = new HashSet<Role>();
            userRoleSet.add(userRole.get());
            user.setRoles(userRoleSet);
            user.setAdmin(admin.get());
            user.setPassword(addPersonDTO.getPersonDetail().getPassword());
            user.setBirthDate(addPersonDTO.getPersonDetail().getBirthDate());
            user.setEmail(addPersonDTO.getPersonDetail().getEmail());
            user.setFirstName(addPersonDTO.getPersonDetail().getFirstName());
            user.setGender(addPersonDTO.getPersonDetail().getGender());
            user.setLastName(addPersonDTO.getPersonDetail().getLastName());
            user.setMaritalStatus(addPersonDTO.getPersonDetail().getMaritualStatus());
            user.setMobileno(addPersonDTO.getPersonDetail().getMobileno());

            user.setCreatedBy(userCreated.get());
            user.setCreatedDate(addPersonDTO.getPersonDetail().getCreatedDate());
            user.setLastModifiedDate(addPersonDTO.getPersonDetail().getUpdatedDate());
            user.setLastModifiedBy(userUpdated.isPresent() ? userUpdated.get() : null);
            user.setStatus(addPersonDTO.getPersonDetail().getStatus());
            user.setIsDeleted(addPersonDTO.getPersonDetail().getIsDeleted());
            // user.setProfilePic(addPersonDTO.getPersonDetail().getProfilePic());
            user.setSurname(addPersonDTO.getPersonDetail().getSurname());
            user.setVillage(village.get());

            User userRes = userRepository.save(user);

            userRes.setMobileLocalId(addPersonDTO.getPersonDetail().getMobileLocalId());

            GetPersonDetail getPersonDetail = new GetPersonDetail();
            getPersonDetail.setAdminId(admin.get().getId());
            getPersonDetail.setAdminName(admin.get().getName());
            getPersonDetail.setBirthDate(addPersonDTO.getPersonDetail().getBirthDate());
            getPersonDetail.setEmail(addPersonDTO.getPersonDetail().getEmail());
            getPersonDetail.setFirstName(addPersonDTO.getPersonDetail().getFirstName());
            getPersonDetail.setGender(addPersonDTO.getPersonDetail().getGender());
            getPersonDetail.setLastName(addPersonDTO.getPersonDetail().getLastName());
            getPersonDetail.setMaritualStatus(addPersonDTO.getPersonDetail().getMaritualStatus());
            getPersonDetail.setMobileno(addPersonDTO.getPersonDetail().getMobileno());

            getPersonDetail.setCreatedBy(userCreated.get().getId());
            getPersonDetail.setCreatedDate(addPersonDTO.getPersonDetail().getCreatedDate());
            getPersonDetail.setUpdatedDate(addPersonDTO.getPersonDetail().getUpdatedDate());
           getPersonDetail.setUpdatedBy(userUpdated.isPresent() ? userUpdated.get().getId() : "");
            getPersonDetail.setStatus(addPersonDTO.getPersonDetail().getStatus());
            getPersonDetail.setIsDeleted(addPersonDTO.getPersonDetail().getIsDeleted());
            // user.setProfilePic(addPersonDTO.getPersonDetail().getProfilePic());
            getPersonDetail.setSurname(addPersonDTO.getPersonDetail().getSurname());
            getPersonDetail.setVillageName(village.get().getName());

            Optional<User> person = userRepository.findById(userRes.getId());
            if (!person.isPresent()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Some thing went wrong", null), HttpStatus.BAD_REQUEST);
            }

            List<Address> addressList = new ArrayList<>();
            List<GetAddressDetail> getAddressDetail = new ArrayList<>();

            for (AddAddressFromUserDTO address : addPersonDTO.getAddress()) {
                Address addressObj = new Address();
                GetAddressDetail getAddress = new GetAddressDetail();
//                Optional<Admin> createdBy = adminRepository.findById(address.getCreatedBy());
//                if (!createdBy.isPresent()) {
//                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "createdBy does not exist in User", null), HttpStatus.BAD_REQUEST);
//                }
//
//                Optional<Admin> updatedBy = adminRepository.findById(address.getUpdatedBy());
//                if (!updatedBy.isPresent()) {
//                    return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "updatedBy does not exist in User", null), HttpStatus.BAD_REQUEST);
//                }
                addressObj.setAddressText(address.getAddressText());
                addressObj.setAddressType(address.getAddressType());
                addressObj.setCountry(coutryRepository.findById(address.getCountryId()).get());
                addressObj.setDistrict(districtRepository.findById(address.getDistrictId()).get());
                addressObj.setPersonId(person.get());
                addressObj.setMobileLocalId(address.getMobileLocalId());
                addressObj.setCreatedBy(admin.get());
                addressObj.setCreatedDate(address.getCreatedDate());
                addressObj.setLastModifiedBy(admin.get());
                addressObj.setLastModifiedDate(address.getUpdatedDate());
                addressObj.setStatus(address.getStatus());
                addressObj.setIsDeleted(address.getIsDeleted());
                Address savedAddress = addressRepository.save(addressObj);
                addressList.add(savedAddress);

                getAddress.setId(savedAddress.getId());
                getAddress.setAddressText(address.getAddressText());
                getAddress.setAddressType(address.getAddressType());
                Optional<Country> country = coutryRepository.findById(address.getCountryId());
                getAddress.setCountry(country.isPresent() ? country.get().getName() : null);
                getAddress.setCountryId(country.isPresent() ? country.get().getId() : null);
                Optional<District> district = districtRepository.findById(address.getDistrictId());
                getAddress.setDistrict(district.isPresent() ? district.get().getName() : null);
                getAddress.setDistrictId(district.isPresent() ? district.get().getId() : null);
                getAddress.setPersonId(person.get().getId());
                getAddress.setMobileLocalId(address.getMobileLocalId());
                getAddress.setCreatedBy(admin.get().getId());
                getAddress.setCreatedDate(address.getCreatedDate());
                getAddress.setUpdatedBy(admin.get().getId());
                getAddress.setUpdatedDate(address.getUpdatedDate());
                getAddress.setStatus(address.getStatus());
                getAddress.setIsDeleted(address.getIsDeleted());

                getAddressDetail.add(getAddress);
            }
            HashMap<String, Object> userRet = new HashMap<>();
            userRet.put(GlobalConstants.BASIC_DETAIL, getPersonDetail);
            userRet.put(GlobalConstants.ADDRESS, getAddressDetail);

            return new ResponseEntity(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userRet), HttpStatus.CREATED);
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
                updatePersonAddress(address, user.get(), createdBy.get(), updatedBy.get());
            }
        }

        Optional<User> updatedUser = userRepository.findById(updateUserDTO.getPersonDetail().getPersonId());
        GetPersonDetail getPersonDetail = new GetPersonDetail();
        List<GetAddressDetail> addressDetailList = new ArrayList<>();
        if(updatedUser.isPresent()){
            getPersonDetail.setAdminId(updatedUser.get().getAdminId());
            getPersonDetail.setAdminName(updatedUser.get().getAdmin().getName());
            getPersonDetail.setBirthDate(updatedUser.get().getBirthDate());
            getPersonDetail.setEmail(updatedUser.get().getEmail());
            getPersonDetail.setFirstName(updatedUser.get().getFirstName());
            getPersonDetail.setGender(updatedUser.get().getGender());
            getPersonDetail.setLastName(updatedUser.get().getLastName());
            getPersonDetail.setMaritualStatus(updatedUser.get().getMaritalStatus());
            getPersonDetail.setMobileno(updatedUser.get().getMobileno());

            getPersonDetail.setCreatedBy(updatedUser.get().getCreatedBy().getId());
            getPersonDetail.setCreatedDate(updatedUser.get().getCreatedDate());
            getPersonDetail.setUpdatedDate(updatedUser.get().getLastModifiedDate());
            getPersonDetail.setUpdatedBy(updatedUser.get().getLastModifiedBy().getId());
            getPersonDetail.setStatus(updatedUser.get().getStatus());
            getPersonDetail.setIsDeleted(updatedUser.get().getIsDeleted());
            // user.setProfilePic(addPersonDTO.getPersonDetail().getProfilePic());
            getPersonDetail.setSurname(updatedUser.get().getSurname());
            getPersonDetail.setVillageName(updatedUser.get().getVillage().getName());
            if(updatedUser.get() != null && updatedUser.get().getAddressList() != null) {
                for (Address address : updatedUser.get().getAddressList()) {
                    GetAddressDetail getAddress = new GetAddressDetail();
                    getAddress.setId(updatedUser.get().getId());
                    getAddress.setAddressText(address.getAddressText());
                    getAddress.setAddressType(address.getAddressType());
                    Optional<Country> country = coutryRepository.findById(address.getCountryId());
                    getAddress.setCountry(country.isPresent() ? country.get().getName() : null);
                    getAddress.setCountryId(country.isPresent() ? country.get().getId() : null);
                    Optional<District> district = districtRepository.findById(address.getDistrictId());
                    getAddress.setDistrict(district.isPresent() ? district.get().getName() : null);
                    getAddress.setDistrictId(district.isPresent() ? district.get().getId() : null);
                    getAddress.setPersonId(updatedUser.get().getId());
                    getAddress.setMobileLocalId(address.getMobileLocalId());
                    getAddress.setCreatedBy(address.getCreatedBy().getId());
                    getAddress.setCreatedDate(address.getCreatedDate());
                    getAddress.setUpdatedBy(address.getLastModifiedBy().getId());
                    getAddress.setUpdatedDate(address.getLastModifiedDate());
                    getAddress.setStatus(address.getStatus());
                    getAddress.setIsDeleted(address.getIsDeleted());
                    addressDetailList.add(getAddress);
                }
            }
        }
        HashMap<String, Object> userRet = new HashMap<>();
        userRet.put(GlobalConstants.BASIC_DETAIL, getPersonDetail);
        userRet.put(GlobalConstants.ADDRESS, addressDetailList);
        return new ResponseEntity(new ApiResponse(HttpStatus.CREATED.value(), true, "User created", userRet), HttpStatus.CREATED);
    }

    private void updatePersonAddress(UpdateAddressFromUserDTO updateUserDTO, User personId, Admin createdBy, Admin updatedBy) {

        Address address = new Address();
        if (addressRepository.existsById(updateUserDTO.getAddressId())) {
            address.setId(updateUserDTO.getAddressId());
        }
        address.setPersonId(personId);
        address.setAddressType(updateUserDTO.getAddressType());
        address.setAddressText(updateUserDTO.getAddressText());
        address.setCountry(coutryRepository.findById(updateUserDTO.getCountryId()).get());
        address.setDistrict(districtRepository.findById(updateUserDTO.getDistrictId()).get());
        address.setMobileLocalId(updateUserDTO.getMobileLocalId());
        address.setCreatedBy(createdBy);
        address.setCreatedDate(updateUserDTO.getCreatedDate());
        address.setLastModifiedBy(updatedBy);
        address.setLastModifiedDate(updateUserDTO.getUpdatedDate());
        address.setStatus(updateUserDTO.getStatus());
        address.setIsDeleted(updateUserDTO.getIsDeleted());
        addressRepository.save(address);

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
        person.setBirthDate(personDetail.getBirthDate());
        person.setEmail(personDetail.getEmail());
        person.setFirstName(personDetail.getFirstName());
        person.setGender(personDetail.getGender());
        person.setLastName(personDetail.getLastName());
        person.setMaritalStatus(personDetail.getMaritualStatus());
        person.setMobileno(personDetail.getMobileno());
        person.setProfilePic(personDetail.getProfilePic());
        person.setSurname(personDetail.getSurname());
        person.setVillage(village.get());
        person.setMobileLocalId(personDetail.getMobileLocalId());

        person.setCreatedBy(userCreated.get());
        person.setCreatedDate(personDetail.getCreatedDate());
        person.setLastModifiedDate(personDetail.getUpdatedDate());
        person.setLastModifiedBy(userUpdated.get());
        person.setStatus(personDetail.getStatus());
        person.setIsDeleted(personDetail.getIsDeleted());

        User userRes = userRepository.save(person);

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
                users.forEach((user) -> {
                    PersonalDetail personalDetail = new PersonalDetail();
                    personalDetail.setBirthDate(user.getBirthDate());
                    personalDetail.setBloodGroup(user.getBloodGroup());
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
                    personalDetail.setIsSync(GlobalConstants.SYNC_STATUS);

                    personalDetail.setIsDelete(String.valueOf(user.getIsDeleted()));
                    personalDetail.setDeletedAt("");
                    personalDetail.setCreatedBy("");
                    personalDetail.setStatus(String.valueOf(user.getStatus()));
                    personalDetail.setUpdatedAt(String.valueOf(user.getLastModifiedDate()));
                    personalDetail.setUpdatedBy(String.valueOf(user.getLastModifiedBy() != null ? user.getLastModifiedBy().getId() : ""));
                    personalDetail.setCreatedBy(String.valueOf(user.getCreatedBy() != null ? user.getCreatedBy().getId() : ""));
                    personalDetail.setCreatedAt(String.valueOf(user.getCreatedDate()));
                    personalDetails.add(personalDetail);


                    List<Address> address = user.getAddressList();
                    address.forEach((add) -> {
                        AddressDetail addressDetail = new AddressDetail();
                        addressDetail.setPersonAddressId(add.getId());
                        addressDetail.setAddressText(add.getAddressText());
                        addressDetail.setType(add.getAddressType());
                        addressDetail.setCountryId(add.getCountryId());
                        addressDetail.setDistrictId(add.getDistrictId());
                        addressDetail.setPersonId(add.getPersonId().getId());

                        addressDetail.setIsDelete(String.valueOf(add.getIsDeleted()));

                        addressDetail.setUpdatedAt(String.valueOf(add.getLastModifiedDate()));
                        addressDetail.setUpdatedBy(String.valueOf(add.getLastModifiedBy()));

                        addressDetail.setCreatedBy(String.valueOf(add.getCreatedBy() != null ? add.getCreatedBy().getId() : null));
                        addressDetail.setCreatedAt(String.valueOf(add.getCreatedDate()));
                        addressDetail.setIsSync(GlobalConstants.SYNC_STATUS);
                        addressDetails.add(addressDetail);
                    });
                });

                retObj.put("Personal Detail", personalDetails);
                retObj.put("Address", addressDetails);
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
