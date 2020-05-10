package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListPersonBasicDetail {
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

}
