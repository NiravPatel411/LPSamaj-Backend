package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPersonDetail {

    private String personId;
    private String adminId;
    private String adminName;
    private String maritualStatus;
    private String villageName;
    private String birthDate;
    private String firstName;
    private String lastName;
    private String surname;
    private String profilePic;
    private String email;
    private String gender;
    private String mobileno;
    private String bloodGroup;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;
    private String mobileLocalId;
}
