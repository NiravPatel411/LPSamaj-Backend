package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetail {

    private String personId;

    private String firstName;

    private String lastName;

    private String surname;

    private String status;

    private String profilePic;

    private String villageId;

    private String email;

    private String gender;

    private LocalDate birthDate;

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

}
