package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonSettingResponseDTO {
    private String personSettingId;
    private Integer adminCanUpdate;
    private Integer profilePictureVisiblity;
    private Integer contactNumberVisibility;
    private Integer bloodDonate;
    private Integer newsNotification;

    public static PersonSettingResponseDTO toDTO(PersonSetting personSetting) {
        return new PersonSettingResponseDTO(
                personSetting.getPersonSettingId(),
                personSetting.getAdminCanUpdate(),
                personSetting.getProfilePictureVisiblity(),
                personSetting.getContactNumberVisibility(),
                personSetting.getBloodDonate(),
                personSetting.getNewsNotification()
        );
    }

}
