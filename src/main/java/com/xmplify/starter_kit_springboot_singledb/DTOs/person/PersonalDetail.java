package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.DTOs.Setting.PersonSettingDTO;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonSettingResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
public class PersonalDetail {

    private String id;
    private String familyCode;
    private String userName;
    private String firstName;
    private String lastName;
    private String surname;
    private MultipartFile profilePic;
    private String profileURL;
    private String villageName;
    private String villageId;
    private String husbandVillageId;
    private String husbandFirstName;
    private String husbandLastName;
    private String husbandSurname;
    private String email;
    private String gender;
    private String birthDate;
    private String bloodGroup;
    private String maritalStatus;
    private String password;
    private String mobileno;
    private String adminId;
    private String hobby;
    private String createdBy;
    private String createdByName;
    private String updatedBy;
    private String updatedByName;
    private String createdAt;
    private String updatedAt;
    private String isDelete;
    private PersonSettingDTO personSetting;



    public static PersonalDetail create(User savedUser, String deleveryPath) {
        PersonalDetail personalDetail =  new PersonalDetail(
                savedUser.getId(),
                savedUser.getFamilyCode(),
                savedUser.getUserName(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getSurname(),
                savedUser.getProfilePic(),
                savedUser.getVillage().getName(),
                savedUser.getVillageId(),
                savedUser.getHusbandVillageId(),
                savedUser.getHusbandFirstName(),
                savedUser.getHusbandLastName(),
                savedUser.getHusbandSurname(),
                savedUser.getEmail(),
                savedUser.getGender(),
                savedUser.getBirthDate().toString(),
                savedUser.getBloodGroup(),
                savedUser.getMaritalStatus(),
                savedUser.getPassword(),
                savedUser.getMobileno(),
                savedUser.getAdminId(),
                savedUser.getHobby(),
                Objects.nonNull(savedUser.getCreatedBy()) ? savedUser.getCreatedBy().getId() : "",
                Objects.nonNull(savedUser.getCreatedBy()) ? savedUser.getCreatedBy().getName(): "",
                Objects.nonNull(savedUser.getUpdatedBy()) ? savedUser.getUpdatedBy().getId(): "",
                Objects.nonNull(savedUser.getUpdatedBy()) ? savedUser.getUpdatedBy().getName(): "",
                Objects.nonNull(savedUser.getCreatedAt()) ? savedUser.getCreatedAt().toString(): "",
                Objects.nonNull(savedUser.getUpdatedAt()) ? savedUser.getUpdatedAt().toString(): "",
                String.valueOf(savedUser.getIsDeleted()),
                PersonSettingDTO.create(savedUser.getPersonSetting())
        );
        personalDetail.setProfileURL(deleveryPath);
        return personalDetail;
    }

    public PersonalDetail(String id, String familyCode, String userName, String firstName, String lastName, String surname, String profileURL, String villageName, String villageId, String husbandVillageId, String husbandFirstName, String husbandLastName, String husbandSurname, String email, String gender, String birthDate, String bloodGroup, String maritalStatus, String password, String mobileno, String adminId, String hobby, String createdBy, String createdByName, String updatedBy, String updatedByName, String createdAt, String updatedAt, String isDelete, PersonSettingDTO personSetting) {
        this.id = id;
        this.familyCode = familyCode;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.profileURL = profileURL;
        this.villageName = villageName;
        this.villageId = villageId;
        this.husbandVillageId = husbandVillageId;
        this.husbandFirstName = husbandFirstName;
        this.husbandLastName = husbandLastName;
        this.husbandSurname = husbandSurname;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.mobileno = mobileno;
        this.adminId = adminId;
        this.hobby = hobby;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.updatedBy = updatedBy;
        this.updatedByName = updatedByName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
        this.personSetting = personSetting;
    }


}
