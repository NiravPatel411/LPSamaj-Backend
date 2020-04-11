package com.xmplify.starter_kit_springboot_singledb;

import com.xmplify.starter_kit_springboot_singledb.payload.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {


    public AddPersonDTO updateUserDTOToAddUserDTO(UpdateUserDTO updatePersonDTO) {
        AddPersonDTO addPersonDTO = new AddPersonDTO();
        addPersonDTO.setPersonDetail(this.updatePersonDetailDTOToAddPersonDetailDTO(updatePersonDTO.getPersonDetail()));
        addPersonDTO.setAddress(this.updateAddressFromUserDTOToAddAddressFromUserDTO(updatePersonDTO.getAddress()));
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
                personDetail.getUpdatedBy(),
                personDetail.getIsDeleted(),
                personDetail.getStatus()
        );


    }
}
