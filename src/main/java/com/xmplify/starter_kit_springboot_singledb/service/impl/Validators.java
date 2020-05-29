package com.xmplify.starter_kit_springboot_singledb.service.impl;

import com.xmplify.starter_kit_springboot_singledb.model.Activity;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeType;
import com.xmplify.starter_kit_springboot_singledb.payload.AddEditMedia;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.PersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.activity.AddActivityRequest;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    CommitteeRepository committeeRepository;

    @Autowired
    CommitteeTypeRepository committeeTypeRepository;


    public List<String> validateAddPersonDTO(AddPersonDTO addPersonDTO) {
        List<String> response = new ArrayList<>();
        if(Objects.nonNull(addPersonDTO)){
            validateAddPersonDetail(addPersonDTO.getPersonDetail(),response);
            validateAddPersonAddress(addPersonDTO.getAddress(),response);
            if(Objects.nonNull(addPersonDTO.getEducation())) {
                validateEducation(addPersonDTO.getEducation(), response);
            }
        } else {
            response.add("Request can not be null");
        }
        return response;
    }

    private void validateEducation(List<EducationDTO> educationDTO, List<String> response) {
        for(int i=0 ; i <educationDTO.size() ; i++){
            if(!validateCreatedBy(educationDTO.get(i).getCreatedBy())){
                response.add("\n Can not find Admin by createdBy ("+educationDTO.get(i).getCreatedBy()+") of education["+i+"]");
            }
            if(!validateDegreeId(educationDTO.get(i).getDegreeId())){
                response.add("\n Can not find Degree by degreeId ("+educationDTO.get(i).getDegreeId()+") of education["+i+"]");
            }
            if(Objects.nonNull(educationDTO.get(i).getUpdatedBy()) && !validateUpdatedBy(educationDTO.get(i).getUpdatedBy())){
                response.add("\n Can not find Admin by updatedBy ("+educationDTO.get(i).getUpdatedBy()+") of education["+i+"]");
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
        for(int i=0 ; i <address.size() ; i++){
            if(!validateContryId(address.get(i).getCountryId())){
                response.add("Can not find Country by countryId ("+address.get(i).getCountryId()+") of address["+i+"]");
            }
            if(!validateStateId(address.get(i).getStateId())){
                response.add("Can not find State by Stateid ("+address.get(i).getStateId()+") of address["+i+"]");
            }
            if(!validateDestrictId(address.get(i).getDistrictId())){
                response.add("Can not find District by districtId ("+address.get(i).getDistrictId()+") of address["+i+"]");
            }
            if(!validateCreatedBy(address.get(i).getCreatedBy())){
                response.add("Can not find Admin by createdBy ("+address.get(i).getCreatedBy()+") of address["+i+"]");
            }
        }
    }

    private boolean validateCreatedBy(String createdBy) {
        return adminRepository.existsById(createdBy);
    }

    private boolean validateDestrictId(String districtId) {
        return districtRepository.existsById(districtId);
    }

    private boolean validateStateId(String stateId) {
        return stateRepository.existsById(stateId);
    }

    private boolean validateContryId(String countryId) {
        return coutryRepository.existsById(countryId);
    }

    private void validateAddPersonDetail(PersonDetailDTO personDetail, List<String> response) {
        if(!validateAdminId(personDetail.getAdminId())){
            response.add("Can not find Admin by adminId ("+personDetail.getAdminId()+") of personDetail");
        }
        if(!validateCreatedBy(personDetail.getCreatedBy())){
            response.add("Can not find Admin by Createdby ("+personDetail.getCreatedBy()+") of personDetail");
        }
        if(validateEmail(personDetail.getEmail())){
            response.add("User already exist by emailId ("+personDetail.getEmail()+") of personDetail");
        }
        if(validateMobileNo(personDetail.getMobileno())){
            response.add("User already exist by mobile no ("+personDetail.getMobileno()+") of personDetail");
        }
        if(!validateVillageId(personDetail.getVillageId())){
            response.add("Can not find Village by villageId ("+personDetail.getVillageId()+") of personDetail");
        }

    }

    private boolean validateVillageId(String villageId) {
        return villageRepository.existsById(villageId);
    }

    private boolean validateMobileNo(String mobileno) {
        return userRepository.existsByMobileno(mobileno);
    }

    private boolean validateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean validateAdminId(String adminId) {
        return adminRepository.existsById(adminId);
    }

    public List<String> validateUpdateActivityRequst(Activity activity) {
        List<String> response = new ArrayList<>();
        validateAdmin(activity.getAdminId(),response);
        validateActivityId(activity.getId(),response);
        return response;
    }

    private void validateActivityId(String id, List<String> response) {
        if (!activityRepository.existsById(id)) {
            response.add("Invalid Activity Id");
        }
    }

    private void validateAdmin(Admin adminId, List<String> response) {
        if(Objects.nonNull(adminId)){
            if(!validateAdminId(adminId.getId())){
                response.add("Can not find Admin by adminId ("+adminId+") of activity");
            }
        } else {
            response.add("Can not find Admin by adminId of activity");
        }
    }

    public List<String> validateAddActivityRequst(Activity activity) {
        List<String> response = new ArrayList<>();
        validateAdmin(activity.getAdminId(),response);
        return response;
    }

    public List<String> validateAddActivityRequestDTO(AddActivityRequest request) {
        List<String> response = new ArrayList<>();
        if(Objects.nonNull(request.getActivityMedia())) {
            validateMedia(request.getActivityMedia(), response);
        }
        return response;

    }

    private void validateMedia(AddEditMedia[] media, List<String> response) {
        for(AddEditMedia addEditMedia : media) {
            MultipartFile file = addEditMedia.getMedia();
            if (file.isEmpty()) {
               response.add("file can not be empty in media");
            }
        }
    }

    public List<String> validateUpdateCommitteeDTO(CommitteeDTO committeeDTO) {
        List<String> response = new ArrayList<>();
        if(!committeeRepository.existsById(committeeDTO.getId())){
            response.add("Id does not exist in CommitteeMember");
        }
        validateCommitteeType(committeeDTO.getCommitteeTypeId(),response);
        validateUserId(committeeDTO.getPersonId(),response);
        return response;
    }

    private void validateUserId(String personId, List<String> response) {
        if(Objects.isNull(personId)){
            response.add("Person Id can not be null or empty");
        } else {
            if(!userRepository.existsById(personId)){
                response.add("Person does not exist by provided person id : "+personId);
            }
        }
    }

    private void validateCommitteeType(String committeeTypeId, List<String> response) {
        if(Objects.isNull(committeeTypeId)){
            response.add("Committee Type Id can not be null or empty");
        }
        if(!committeeTypeRepository.existsById(committeeTypeId)){
            response.add("Invalid/Does not exist Committee Type Id.");
        }
    }

    public List<String> validateAddCommitteeDTO(CommitteeDTO committeeDTO) {
        List<String> response = new ArrayList<>();
        validateCommitteeType(committeeDTO.getCommitteeTypeId(),response);
        validateUserId(committeeDTO.getPersonId(),response);
        return response;
    }

    public List<String> validateAddCommitteeType(CommitteeType committeeType) {
        List<String> response = new ArrayList<>();
        validateUniqueTypeOfCommitteeType(committeeType.getType(),response);
        return response;
    }

    private void validateUniqueTypeOfCommitteeType(String type, List<String> response) {
        if(Objects.isNull(type)){
            response.add("Type can not be null");
        }
        if(committeeTypeRepository.existsByType(type)){
           response.add("Type already exist");
        }
    }

    public List<String> validateUpdateCommitteeType(CommitteeType committeeType) {
        List<String> response = new ArrayList<>();
        validateCommitteeId(committeeType.getId(),response);
        validateUniqueTypeOfCommitteeType(committeeType.getType(),response);
        return response;
    }

    private void validateCommitteeId(String id, List<String> response) {
        if(!committeeTypeRepository.existsById(id)){
            response.add("Invalid Id.");
        }
    }
}
