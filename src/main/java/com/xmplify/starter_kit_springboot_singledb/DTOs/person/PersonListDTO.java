package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.Utility;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonListDTO {
    private String personId;
    private String firstName;
    private String lastName;
    private String surname;
    private String profilePic;
    private String husbandVillageName;
    private String husbandFirstName;
    private String husbandLastName;
    private String husbandSurname;
    private String villageName;
    private String email;
    private String familyCode;
    private String gender;
    private String mobileno;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String createdByName;
    private String updatedBy;
    private String updateByName;
    private String isDeleted;
    private String status;

    public static List<PersonListDTO> create(List<User> users) {
        List<PersonListDTO> personListDTOS = new ArrayList<>();
        users.forEach((user) -> personListDTOS.add(create(user)));
        return personListDTOS;
    }

    public static PersonListDTO create(User user) {
        return new PersonListDTO(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getSurname(),
                Objects.nonNull(user.getProfilePic()) ? Utility.fileService.getDeleveryPath(user.getProfilePic(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT) : "",
                "",//Todo : put the foreign key of village in husbund Village Id
                user.getHusbandFirstName(),
                user.getHusbandLastName(),
                user.getHusbandSurname(),
                user.getVillage().getName(),
                user.getEmail(),
                user.getFamilyCode(),
                user.getGender(),
                user.getMobileno(),
                Objects.nonNull(user.getCreatedAt()) ? user.getCreatedAt().toString() : "",
                Objects.nonNull(user.getUpdatedBy()) ? user.getUpdatedBy().getName() : "",
                Objects.nonNull(user.getCreatedBy()) ? user.getCreatedBy().getId() : "",
                Objects.nonNull(user.getCreatedBy()) ? user.getCreatedBy().getName() : "",
                Objects.nonNull(user.getUpdatedBy()) ? user.getUpdatedBy().getId() : "",
                Objects.nonNull(user.getUpdatedBy()) ? user.getUpdatedBy().getName() : "",
                String.valueOf(user.getIsDeleted()),
                user.getStatus());
    }
}
