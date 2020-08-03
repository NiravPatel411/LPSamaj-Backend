package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Setting.PersonSettingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PersonBasicDetailDTO {
    PersonalDetailDTO personDetail;
    PersonSettingDTO personSetting;
    List<AddressDTO> addresses;

    public static PersonBasicDetailDTO create(PersonalDetailDTO personalDetailDTO, List<AddressDTO> returnAddressDTO, PersonSettingDTO personSettingDTO) {
        return new PersonBasicDetailDTO(personalDetailDTO, personSettingDTO,returnAddressDTO);
    }
}
