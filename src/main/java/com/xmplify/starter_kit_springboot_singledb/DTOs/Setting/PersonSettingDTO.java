package com.xmplify.starter_kit_springboot_singledb.DTOs.Setting;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonSettingDTO {
    private String personSettingId;
    private Integer adminCanUpdate;
    private Integer profilePictureVisiblity;
    private Integer contactNumberVisibility;
    private Integer bloodDonate;
    private Integer newsNotification;

    public static PersonSettingDTO create(PersonSetting personSetting) {
        return new PersonSettingDTO(
                personSetting.getPersonSettingId(),
                personSetting.getAdminCanUpdate(),
                personSetting.getProfilePictureVisiblity(),
                personSetting.getContactNumberVisibility(),
                personSetting.getBloodDonate(),
                personSetting.getNewsNotification()
        );
    }

}
