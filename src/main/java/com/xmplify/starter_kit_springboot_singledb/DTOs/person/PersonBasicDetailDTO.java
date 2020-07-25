package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PersonBasicDetailDTO {
    PersonalDetail personDetail;
    List<AddressDTO> addresses;

    public static PersonBasicDetailDTO create(PersonalDetail personalDetail, List<AddressDTO> returnAddressDTO) {
        return new PersonBasicDetailDTO(personalDetail, returnAddressDTO);
    }
}
