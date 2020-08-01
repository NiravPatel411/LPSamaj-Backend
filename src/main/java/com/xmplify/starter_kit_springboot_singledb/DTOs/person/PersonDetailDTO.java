package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonDetailDTO {

    PersonalDetailDTO personalDetailDTO;
    List<AddressDTO> addresses;
    List<EducationDTO> educations;

    public static PersonDetailDTO create(PersonalDetailDTO personalDetailDTO, List<AddressDTO> returnAddressDTO, List<EducationDTO> educationDTOList) {
        return new PersonDetailDTO(personalDetailDTO,returnAddressDTO,educationDTOList);
    }
}
