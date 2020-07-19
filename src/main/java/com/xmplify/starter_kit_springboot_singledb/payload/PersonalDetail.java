package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload.PersonDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetail {

    private String personId;

    private String firstName;

    private String lastName;

    private String husbandVillageId;

    private String husbandFirstName;

    private String husbandLastName;

    private String husbandSurname;

    private String surname;

    private String status;

    private String profilePic;

    private String villageId;

    private String villageName;

    private String email;

    private String gender;

    private String birthDate;

    private String bloodGroup;

    private String maritalStatus;

    private String contactNo;


    private String deletedBy;

    private String createdBy;

    private String updatedBy;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;

    private String isDelete;

    private String isSync;

    private PersonSettingResponseDTO personSetting;

    public static PersonalDetail toDTO(User user){
        PersonSettingResponseDTO personSettingDTO = PersonSettingResponseDTO.toDTO(user.getPersonSetting());
        return new PersonalDetail(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getHusbandVillageId(),
                user.getHusbandFirstName(),
                user.getHusbandLastName(),
                user.getHusbandSurname(),
                user.getSurname(),
                user.getStatus(),
                user.getProfilePic(),
                user.getVillage().getId(),
                user.getVillage().getName(),
                user.getEmail(),
                user.getGender(),
                user.getBirthDate().toString(),
                user.getBloodGroup(),
                user.getMaritalStatus(), user.getMobileno(),
                "",
                Objects.nonNull(user.getCreatedBy()) ? user.getCreatedBy().getId() : "",
                Objects.nonNull(user.getUpdatedBy()) ? user.getUpdatedBy().getId() : "",
                Objects.nonNull(user.getCreatedAt()) ? user.getCreatedAt().toString() : "",
                Objects.nonNull(user.getUpdatedAt()) ? user.getUpdatedAt().toString() : "",
                Objects.nonNull(user.getDeletedAt()) ? user.getDeletedAt().toString() : "",
                String.valueOf(user.getIsDeleted()),
                "",
                personSettingDTO);
    }

}
