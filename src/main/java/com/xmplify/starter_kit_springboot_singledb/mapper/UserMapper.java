package com.xmplify.starter_kit_springboot_singledb.mapper;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Address;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.GetAddressDetail;
import com.xmplify.starter_kit_springboot_singledb.payload.GetPersonDetail;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.AddPersonDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.PersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateAddressFromUserDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdatePersonDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload.UpdateUserDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.*;
import com.xmplify.starter_kit_springboot_singledb.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class UserMapper {

    @Autowired
    VillageRepository villageRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CoutryRepository coutryRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateRepository stateRepository;


    public AddPersonDTO updateUserDTOToAddUserDTO(UpdateUserDTO updatePersonDTO) {
        AddPersonDTO addPersonDTO = new AddPersonDTO();
        addPersonDTO.setPersonDetail(this.updatePersonDetailDTOToAddPersonDetailDTO(updatePersonDTO.getPersonDetail()));
        addPersonDTO.setAddress(this.updateAddressFromUserDTOToAddAddressFromUserDTO(updatePersonDTO.getAddress()));
        addPersonDTO.setEducation(updatePersonDTO.getEducation());
        return addPersonDTO;
    }

    private List<AddAddressFromUserDTO> updateAddressFromUserDTOToAddAddressFromUserDTO(List<UpdateAddressFromUserDTO> address) {
        List<AddAddressFromUserDTO> addAddressFromUserDTOS = new ArrayList<>();
        for(UpdateAddressFromUserDTO updateAddressFromUserDTO : address){
            addAddressFromUserDTOS.add(new AddAddressFromUserDTO(
                    updateAddressFromUserDTO.getAddressType(),
                    updateAddressFromUserDTO.getAddressText(),
                    updateAddressFromUserDTO.getCountryId(),
                    updateAddressFromUserDTO.getDistrictId(),
                    updateAddressFromUserDTO.getStateId(),
                    updateAddressFromUserDTO.getMobileLocalId(),
                    updateAddressFromUserDTO.isSync(),
                    updateAddressFromUserDTO.getCreatedDate(),
                    updateAddressFromUserDTO.getUpdatedDate(),
                    updateAddressFromUserDTO.getCreatedBy(),
                    updateAddressFromUserDTO.getUpdatedBy(),
                    updateAddressFromUserDTO.getIsDeleted(),
                    updateAddressFromUserDTO.getStatus()
            ));
        }
        return addAddressFromUserDTOS;
    }

    private PersonDetailDTO updatePersonDetailDTOToAddPersonDetailDTO(UpdatePersonDetailDTO personDetail) {
        return new PersonDetailDTO(
                personDetail.getFirstName(),
                personDetail.getLastName(),
                personDetail.getSurname(),
                personDetail.getProfilePic(),
                personDetail.getVillageId(),
                personDetail.getEmail(),
                personDetail.getGender(),
                personDetail.getBirthDate(),
                personDetail.getBloodGroup(),
                personDetail.getMaritualStatus(),
                personDetail.getPassword(),
                personDetail.getMobileno(),
                personDetail.getAdminId(),
                personDetail.getMobileLocalId(),
                personDetail.isSync(),
                personDetail.getCreatedDate(),
                personDetail.getUpdatedDate(),
                personDetail.getCreatedBy(),
                personDetail.getPersonId(),
                personDetail.getUpdatedBy(),
                personDetail.getIsDeleted(),
                personDetail.getStatus()
        );

    }


    public User personDetailDTOtoUser(PersonDetailDTO personDetail) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        User user =  new User(null,
                personDetail.getFirstName(),
                personDetail.getLastName(),
                personDetail.getSurname(),
                personDetail.getProfilePic(),
                villageRepository.findById(personDetail.getVillageId()).get(),
                personDetail.getVillageId(),
                personDetail.getEmail(),
                personDetail.getGender(),
                LocalDate.parse(personDetail.getBirthDate(),df),
                personDetail.getBloodGroup(),
                personDetail.getMaritualStatus(),
                personDetail.getPassword(),
                personDetail.getMobileno(),
                roleService.toSet(roleService.getNormalUserRole()),
                null,
                null,
                null,
                adminRepository.findById(personDetail.getAdminId()).get(),
                personDetail.getAdminId(),
                personDetail.getMobileLocalId(),
                null
        );
        user.setUpdatedBy(Objects.nonNull(personDetail.getUpdatedBy()) ?
                adminRepository.findById(personDetail.getUpdatedBy()).isPresent() ?
                        adminRepository.findById(personDetail.getUpdatedBy()).get() :
                        null :
                null);
        user.setCreatedBy(Objects.nonNull(personDetail.getCreatedBy())?
                adminRepository.findById(personDetail.getCreatedBy()).isPresent()?
                        adminRepository.findById(personDetail.getCreatedBy()).get():
                        null:
                null);
        return user;
    }

    public Address addAddressFromUserDTOtoAddress(AddAddressFromUserDTO address, User user) {
        Address newAddress =  new Address(
                null,
                user,
                address.getAddressType(),
                address.getAddressText(),
                coutryRepository.findById(address.getCountryId()).get(),
                address.getCountryId(),
                districtRepository.findById(address.getDistrictId()).get(),
                address.getDistrictId(),
                stateRepository.findById(address.getStateId()).get(),
                address.getStateId(),
                address.getMobileLocalId()
        );
//        newAddress.setCreatedBy(adminRepository.findById(address.getCreatedBy()).get());
        newAddress.setCreatedBy(Objects.nonNull(address.getCreatedBy())?
                adminRepository.findById(address.getCreatedBy()).isPresent()?
                        adminRepository.findById(address.getCreatedBy()).get():
                        null:
                null);
        newAddress.setUpdatedBy(Objects.nonNull(address.getCreatedBy()) ?
                adminRepository.findById(address.getUpdatedBy()).isPresent() ?
                        adminRepository.findById(address.getUpdatedBy()).get() :
                        null :
                null);
        return newAddress;
    }

    public HashMap<String, Object> userToAddPersonResponse(User fromDbUser, AddPersonDTO addPersonDTO) {
        HashMap<String, Object> userRet = new HashMap<>();
        GetPersonDetail getPersonDetail = getPersonDetailFromUser(fromDbUser);
        getPersonDetail.setMobileLocalId(addPersonDTO.getPersonDetail().getMobileLocalId());
        List<GetAddressDetail> getAddressDetails = new ArrayList<>();
        for(Address address : fromDbUser.getAddressList()) {
            GetAddressDetail getAddressDetail = getAddressDetails(address);
            getAddressDetail.setMobileLocalId(address.getMobileLocalId());
            getAddressDetails.add(getAddressDetail);
        }
        userRet.put(GlobalConstants.BASIC_DETAIL, getPersonDetail);
        userRet.put(GlobalConstants.ADDRESS, getAddressDetails);
        return userRet;
    }

    private GetAddressDetail getAddressDetails(Address address) {
        return new GetAddressDetail(address.getId(),
                address.getPersonId().getId(),
                address.getAddressType(),
                address.getAddressText(),
                address.getCountry().getName(),
                address.getCountry().getId(),
                address.getDistrict().getName(),
                address.getDistrict().getId(),
                address.getState().getName(),
                address.getState().getId(),
                "",
                Objects.nonNull(address.getCreatedAt())?address.getCreatedAt().toString():"",
                Objects.nonNull(address.getUpdatedAt())?address.getUpdatedAt().toString():"",
                Objects.nonNull(address.getCreatedBy())?address.getCreatedBy().getId() : "",
                Objects.nonNull(address.getUpdatedBy())?address.getUpdatedBy().getId() : "",
                address.getIsDeleted(),
                address.getStatus());
    }

    private GetPersonDetail getPersonDetailFromUser(User fromDbUser) {
        return new GetPersonDetail(
                fromDbUser.getId(),
                fromDbUser.getAdminId(),
                fromDbUser.getAdmin().getName(),
                fromDbUser.getMaritalStatus(),
                fromDbUser.getVillage().getName(),
                fromDbUser.getBirthDate().toString(),
                fromDbUser.getFirstName(),
                fromDbUser.getLastName(),
                fromDbUser.getSurname(),
                fromDbUser.getProfilePic(),
                fromDbUser.getEmail(),
                fromDbUser.getGender(),
                fromDbUser.getMobileno(),
                fromDbUser.getBloodGroup(),
                Objects.nonNull(fromDbUser.getCreatedAt())?fromDbUser.getCreatedAt().toString():"",
                Objects.nonNull(fromDbUser.getUpdatedAt())?fromDbUser.getUpdatedAt().toString():"",
                Objects.nonNull(fromDbUser.getCreatedBy())?fromDbUser.getCreatedBy().getId() : "",
                Objects.nonNull(fromDbUser.getUpdatedBy())?fromDbUser.getUpdatedBy().getId() : "",
                fromDbUser.getIsDeleted(),
                fromDbUser.getStatus(),
                null
        );
    }
}
