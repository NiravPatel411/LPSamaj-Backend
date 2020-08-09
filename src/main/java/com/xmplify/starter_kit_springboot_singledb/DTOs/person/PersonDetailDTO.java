package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Setting.PersonSettingDTO;
import com.xmplify.starter_kit_springboot_singledb.DTOs.achievement.AchievementDTO;
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

    PersonalDetailDTO personalDetails;
    PersonSettingDTO personSetting;
    List<PersonListDTO> familyPersons;
    List<AddressDTO> addressDetails;
    List<EducationDTO> educationDetails;
    List<AchievementDTO> achievementDetails;

    public static PersonDetailDTO create(PersonalDetailDTO personalDetailDTO, PersonSettingDTO personSettingDTO, List<AddressDTO> returnAddressDTO, List<EducationDTO> educationDTOList, List<PersonListDTO> familyUsers, List<AchievementDTO> achievementDTOList) {
        return new PersonDetailDTO(personalDetailDTO,personSettingDTO,familyUsers,returnAddressDTO,educationDTOList,achievementDTOList);
    }
}
