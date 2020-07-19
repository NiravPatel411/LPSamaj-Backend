package com.xmplify.starter_kit_springboot_singledb.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonBasicDetailDTO1 {
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
    private String gender;
    private String mobileno;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;

    public PersonBasicDetailDTO1(String personId, String firstName, String lastName, String surname, String profilePic, String husbandVillageName, String husbandFirstName, String husbandLastName, String husbandSurname, String villageName, String email, String gender, String mobileno) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.profilePic = profilePic;
        this.husbandVillageName = husbandVillageName;
        this.husbandFirstName = husbandFirstName;
        this.husbandLastName = husbandLastName;
        this.husbandSurname = husbandSurname;
        this.villageName = villageName;
        this.email = email;
        this.gender = gender;
        this.mobileno = mobileno;
    }
}
