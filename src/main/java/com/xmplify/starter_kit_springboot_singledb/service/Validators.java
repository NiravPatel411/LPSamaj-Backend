package com.xmplify.starter_kit_springboot_singledb.service;

import com.google.common.base.Strings;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.achievement.AchievementDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonBasicDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import com.xmplify.starter_kit_springboot_singledb.payload.AddEditMedia;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.PersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDBDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class Validators {

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    DegreeRepository degreeRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CommitteeMemberRepository committeeMemberRepository;

    @Autowired
    CommitteeTypeRepository committeeTypeRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EducationRepository educationRepository;

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    PersonAchievementRepository personAchievementRepository;


    public List<String> validateAddPersonDTO(AddPersonDTO addPersonDTO) {
        List<String> response = new ArrayList<>();
        if (Objects.nonNull(addPersonDTO)) {
            validateAddPersonDetail(addPersonDTO.getPersonDetail(), response);
            validateAddPersonAddress(addPersonDTO.getAddress(), response);
            if (Objects.nonNull(addPersonDTO.getEducation())) {
                validateEducation(addPersonDTO.getEducation(), response);
            }
        } else {
            response.add("Request can not be null");
        }
        return response;
    }

    private void validateEducation(List<EducationDBDTO> educationDTO, List<String> response) {
        for (int i = 0; i < educationDTO.size(); i++) {
            if (!validateCreatedBy(educationDTO.get(i).getCreatedBy())) {
                response.add("\n Can not find Admin by createdBy (" + educationDTO.get(i).getCreatedBy() + ") of education[" + i + "]");
            }
            if (!validateDegreeId(educationDTO.get(i).getDegreeId())) {
                response.add("\n Can not find Degree by degreeId (" + educationDTO.get(i).getDegreeId() + ") of education[" + i + "]");
            }
            if (Objects.nonNull(educationDTO.get(i).getUpdatedBy()) && !validateUpdatedBy(educationDTO.get(i).getUpdatedBy())) {
                response.add("\n Can not find Admin by updatedBy (" + educationDTO.get(i).getUpdatedBy() + ") of education[" + i + "]");
            }
        }
    }

    private boolean validateUpdatedBy(String updatedBy) {
        return adminRepository.existsById(updatedBy);
    }

    private boolean validateDegreeId(String degreeId) {
        return degreeRepository.existsById(degreeId);
    }

    private void validateAddPersonAddress(List<AddAddressFromUserDTO> address, List<String> response) {
        for (int i = 0; i < address.size(); i++) {
            if (!validateContryId(address.get(i).getCountryId())) {
                response.add("Can not find Country by countryId (" + address.get(i).getCountryId() + ") of address[" + i + "]");
            }
            if (!validateStateId(address.get(i).getStateId())) {
                response.add("Can not find State by Stateid (" + address.get(i).getStateId() + ") of address[" + i + "]");
            }
            if (!validateDestrictId(address.get(i).getDistrictId())) {
                response.add("Can not find District by districtId (" + address.get(i).getDistrictId() + ") of address[" + i + "]");
            }
            if (!validateCreatedBy(address.get(i).getCreatedBy())) {
                response.add("Can not find Admin by createdBy (" + address.get(i).getCreatedBy() + ") of address[" + i + "]");
            }
        }
    }

    private boolean validateCreatedBy(String createdBy) {
        return adminRepository.existsById(createdBy);
    }

    private boolean validateDestrictId(String districtId) {
        if(Objects.isNull(districtId)){
            return false;
        }
        return districtRepository.existsById(districtId);
    }

    private boolean validateStateId(String stateId) {
        if(Objects.isNull(stateId)){
            return false;
        }
        return stateRepository.existsById(stateId);
    }

    private boolean validateContryId(String countryId) {
        if(Objects.isNull(countryId)){
            return false;
        }
        return coutryRepository.existsById(countryId);
    }

    private void validateAddPersonDetail(PersonDetailDTO personDetail, List<String> response) {
        if (!validateAdminId(personDetail.getAdminId())) {
            response.add("Can not find Admin by adminId (" + personDetail.getAdminId() + ") of personDetail");
        }
        if (!validateCreatedBy(personDetail.getCreatedBy())) {
            response.add("Can not find Admin by Createdby (" + personDetail.getCreatedBy() + ") of personDetail");
        }
        if (validateEmail(personDetail.getEmail())) {
            response.add("User already exist by emailId (" + personDetail.getEmail() + ") of personDetail");
        }
        if (validateMobileNo(personDetail.getMobileno())) {
            response.add("User already exist by mobile no (" + personDetail.getMobileno() + ") of personDetail");
        }
        if (!validateVillageId(personDetail.getVillageId())) {
            response.add("Can not find Village by villageId (" + personDetail.getVillageId() + ") of personDetail");
        }

    }

    public boolean validateVillageId(String villageId) {
        if(Objects.isNull(villageId)){
            return false;
        }
        return villageRepository.existsById(villageId);
    }

    private boolean validateMobileNo(String mobileno) {
        //return userRepository.existsByMobileno(mobileno);
        return true;
    }

    private boolean validateEmail(String email) {
        if(Objects.isNull(email)){
            return true;
        }
        return userRepository.existsByEmail(email);
    }

    private boolean validateAdminId(String adminId) {
        if(Objects.isNull(adminId)){
            return false;
        }
        return adminRepository.existsById(adminId);
    }

    public List<String> validateUpdateActivityRequst(Activity activity) {
        List<String> response = new ArrayList<>();
        validateAdmin(activity.getAdminId(), response);
        validateActivityId(activity.getId(), response);
        return response;
    }

    private void validateActivityId(String id, List<String> response) {
        if (!activityRepository.existsById(id)) {
            response.add("Invalid Activity Id");
        }
    }

    private void validateAdmin(Admin adminId, List<String> response) {
        if (Objects.nonNull(adminId)) {
            if (!validateAdminId(adminId.getId())) {
                response.add("Can not find Admin by adminId (" + adminId + ") of activity");
            }
        } else {
            response.add("Can not find Admin by adminId of activity");
        }
    }

    public List<String> validateAddActivityRequst(Activity activity) {
        List<String> response = new ArrayList<>();
        validateAdmin(activity.getAdminId(), response);
        return response;
    }

    public List<String> validateAddActivityRequestDTO(AddActivityRequest request) {
        List<String> response = new ArrayList<>();
        if (Objects.nonNull(request.getActivityMedia())) {
            validateMedia(request.getActivityMedia(), response);
        }
        return response;

    }

    private void validateMedia(AddEditMedia[] media, List<String> response) {
        for (AddEditMedia addEditMedia : media) {
            MultipartFile file = addEditMedia.getMedia();
            if (file.isEmpty()) {
                response.add("file can not be empty in media");
            }
        }
    }

    public List<String> validateUpdateCommitteeDTO(CommitteeDTO committeeDTO) {
        List<String> response = new ArrayList<>();
        if (!committeeMemberRepository.existsById(committeeDTO.getId())) {
            response.add("Id does not exist in CommitteeMember");
        }
        validateCommitteeType(committeeDTO.getCommitteeTypeId(), response);
        validateCommitteeMember(committeeDTO, response);
        validateUserId(committeeDTO.getPersonId(), response);
        return response;
    }

    private void validateCommitteeMember(CommitteeDTO committeeDTO, List<String> response) {
        if (committeeTypeRepository.existsById(committeeDTO.getCommitteeTypeId())) {
            CommitteeType newCommitteeType = committeeTypeRepository.findById(committeeDTO.getCommitteeTypeId()).get();
            CommitteeType oldCommitteeType = committeeMemberRepository.findById(committeeDTO.getId()).get().getCommitteeType();
            if (!newCommitteeType.getId().equalsIgnoreCase(oldCommitteeType.getId())) {
                validateMember(committeeDTO, response);
            }
        }
    }

    public void validateUserId(String personId, List<String> response) {
        if (Objects.isNull(personId)) {
            response.add("Person Id can not be null or empty");
        } else {
            if (!userRepository.existsById(personId)) {
                response.add("Person does not exist by provided person id : " + personId);
            }
        }
    }

    private void validateCommitteeType(String committeeTypeId, List<String> response) {
        if (Objects.isNull(committeeTypeId)) {
            response.add("Committee Type Id can not be null or empty");
        }
        if (!committeeTypeRepository.existsById(committeeTypeId)) {
            response.add("Invalid/Does not exist Committee Type Id.");
        }
    }

    public List<String> validateAddCommitteeDTO(CommitteeDTO committeeDTO) {
        List<String> response = new ArrayList<>();
        validateCommitteeType(committeeDTO.getCommitteeTypeId(), response);
        validateMember(committeeDTO, response);
        validateUserId(committeeDTO.getPersonId(), response);
        return response;
    }

    private void validateMember(CommitteeDTO committeeDTO, List<String> response) {
        if (committeeMemberRepository.existsByCommitteeTypeIdAndUserIdId(committeeDTO.getCommitteeTypeId(), committeeDTO.getPersonId())) {
            response.add("person already exist in this committee Type");
        }
    }

    public List<String> validateAddCommitteeType(CommitteeType committeeType) {
        List<String> response = new ArrayList<>();
        validateUniqueTypeOfCommitteeType(committeeType.getType(), response);
        return response;
    }

    private void validateUniqueTypeOfCommitteeType(String type, List<String> response) {
        if (Objects.isNull(type)) {
            response.add("Type can not be null");
        }
        if (committeeTypeRepository.existsByType(type)) {
            response.add("Type already exist");
        }
    }

    public List<String> validateUpdateCommitteeType(CommitteeType committeeType) {
        List<String> response = new ArrayList<>();
        validateCommitteeId(committeeType.getId(), response);
        validateUniqueTypeOfCommitteeType(committeeType.getType(), response);
        return response;
    }

    private void validateCommitteeId(String id, List<String> response) {
        if (!committeeTypeRepository.existsById(id)) {
            response.add("Invalid Id.");
        }
    }

    public boolean isExistAdmin(String adminId) {
        return validateAdminId(adminId);
    }

    public List<String> validateAddPersonBasicDetailDTO(PersonBasicDetailDTO personBasicDetailDTO) {
        List<String> response = new ArrayList<>();
        if(Objects.isNull(personBasicDetailDTO)){
            response.add("Request can not be null");
            return response;
        }

        if(Objects.isNull(personBasicDetailDTO.getPersonDetail())){
            response.add("Invalid request. Personal Detail Missing");
            return response;
        }

        if(Objects.isNull(personBasicDetailDTO.getAddresses())){
            response.add("Invalid request. Address Detail Missing");
            return response;
        }

        if(Objects.nonNull(personBasicDetailDTO.getPersonDetail().getId())){
            response.add("Id should be null or empty in person detail.");
        }
        validatePersonalDetailDTO(personBasicDetailDTO.getPersonDetail(),response);
        for(AddressDTO addressDTO : personBasicDetailDTO.getAddresses()){
            if(Objects.nonNull(addressDTO.getId())){

                response.add("Id should be null or empty in address details");
            }
            validateAddressDTO(addressDTO,response);
        }

        return response;
    }

    private void validateAddressDTO(AddressDTO addressDetails, List<String> response) {
        if(!validateContryId(addressDetails.getCountryId())){
            response.add("Invalid Country Id");
        }

        if(!validateDestrictId(addressDetails.getDistrictId())){
            response.add("Invalid District Id");
        }

        if(!validateStateId(addressDetails.getStateId())){
            response.add("Invalid State Id");
        }

    }

    private void validatePersonalDetailDTO(PersonalDetailDTO personDetail, List<String> response) {

        if(!validateFamilyCode(personDetail.getFamilyCode())){
            response.add("Invalid family code");
        }
        /*
        if(personDetail.getId()==null && !validateUpdateEmail(personDetail.getId(),personDetail.getEmail())){
            response.add("Invalid Email Id");
        }
*/
        if(!validateUpdateUsename(personDetail.getId(),personDetail.getUserName())){
            response.add("Invalid Username");
        }

        if(!validateVillageId(personDetail.getVillageId())){
            response.add("Invalid village id");
        }

        if(Objects.nonNull(personDetail.getHusbandVillageId())){
            if(!validateVillageId(personDetail.getHusbandVillageId()));
        }

        if(!validateAdminId(personDetail.getAdminId())){
            response.add("Invalid Admin Id");
        }
    }

    private boolean validateUpdateUsename(String id, String username) {
        if(Objects.isNull(username)){
            return false;
        }
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isPresent()){
            if(user.get().getId().equalsIgnoreCase(id)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean validateUpdateEmail(String id, String email) {
        if(Objects.isNull(email)){
            return true;
        }
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            if(user.get().getId().equalsIgnoreCase(id)){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean validateUsename(String userName) {
        if(Objects.isNull(userName)){
            return false;
        }
        if(userRepository.existsByUserName(userName)){
            return false;
        }
        return true;
    }

    private boolean validateFamilyCode(String familyCode) {
        if(Objects.isNull(familyCode)){
            return false;
        }
        return true;
    }

    public List<String> validateUpdatePersonBasicDetailDTO(PersonBasicDetailDTO personBasicDetailDTO) {
        List<String> response = new ArrayList<>();
        if(Objects.isNull(personBasicDetailDTO)){
            response.add("Request can not be null");
            return response;
        }

        if(Objects.isNull(personBasicDetailDTO.getPersonDetail())){
            response.add("Invalid request. Personal Detail Missing");
            return response;
        }

        if(Objects.isNull(personBasicDetailDTO.getAddresses())){
            response.add("Invalid request. Address Detail Missing");
            return response;
        }

        validateUserId(personBasicDetailDTO.getPersonDetail().getId(),response);

        validatePersonalDetailDTO(personBasicDetailDTO.getPersonDetail(),response);
        for(AddressDTO addressDTO : personBasicDetailDTO.getAddresses()){
            if(Objects.nonNull(addressDTO.getId())) {
                if (!validateAddressId(addressDTO.getId())) {
                    response.add("Invalid address Id. Can not found any address : "+addressDTO.getId());
                }
            }
            validateAddressDTO(addressDTO,response);
        }

        return response;
    }
    private boolean validateAddressId(String id) {
        return addressRepository.existsById(id);
    }

    public List<String> validateAddEducationDTO(EducationDTO educationDTO) {
        List<String> response = new ArrayList<>();
       validateUserId(educationDTO.getPersonId(),response);
       if(Objects.nonNull(educationDTO.getId()) && validateEducationId(educationDTO.getId())){
           response.add("Invalid Education Id : "+educationDTO.getId());
       }
       if(Objects.nonNull(educationDTO.getDegreeId()) && !validateDegreeId(educationDTO.getDegreeId())) {
           response.add("Invalid Degree Id : " + educationDTO.getDegreeId());
       }
        if(Objects.nonNull(educationDTO.getDegreeId())) {
            Optional<Degree> degree = degreeRepository.findById(educationDTO.getDegreeId());
            if(degree.isPresent() && degree.get().isProofNedded()){
                if(Objects.isNull(educationDTO.getProofPhoto()) && Objects.isNull(educationDTO.getId())){
                    response.add("proof must be need for this Degree");
                }
            }
        }

       return response;
    }

    private boolean validateEducationId(String id) {
        return ! educationRepository.existsById(id);
    }

    public List<String> validateListEducationDTO(List<EducationDTO> educationDTOs) {
        List<String> response = new ArrayList<>();
        for(EducationDTO educationDTO : educationDTOs){
            response.addAll(validateAddEducationDTO(educationDTO));
        }
        return response;
    }

    public boolean validateAchievementName(String name) {
        if(Strings.isNullOrEmpty(name)){
            return false;
        }
        return achievementRepository.existsByName(name);
    }

    public boolean validateAchievementId(String id) {
        if(Strings.isNullOrEmpty(id)){
            return false;
        }
        return achievementRepository.existsById(id);
    }

    public boolean validateAchievementNameWithId(String id, String name) {
        if(Strings.isNullOrEmpty(name)){
            return false;
        }
        Optional<Achievement> achievement = achievementRepository.findByName(name);
        if(achievement.isPresent() && !id.equalsIgnoreCase(achievement.get().getId())){
            return false;
        } else {
            return true;
        }
    }

    public List<String> validateAchievementDto(AchievementDTO achievementDTO) {
        List<String> response = new ArrayList<>();
        if(Objects.isNull(achievementDTO)){
            response.add("request can not be null or empty");
        }

        if(Objects.nonNull(achievementDTO.getId()) && !validatePersonAchievementId(achievementDTO.getId())){
            response.add("InValid id");
        }
        validateUserId(achievementDTO.getPersonId(),response);
        Optional<Achievement> achievement = achievementRepository.findById(achievementDTO.getAchievementTypeId());
        if(achievement.isPresent()){
            if (!Objects.nonNull(achievementDTO.getId()) && achievement.get().isProofNedded() && Objects.isNull(achievementDTO.getProofPhoto())) {
               response.add("achievement proof must be required");
            }
        } else {
            response.add("Invalid Achievement Type Id");
        }
        return response;
    }

    private boolean validatePersonAchievementId(String id) {
        if(!Objects.nonNull(id)){
            return false;
        }
        return personAchievementRepository.existsById(id);
    }
}
