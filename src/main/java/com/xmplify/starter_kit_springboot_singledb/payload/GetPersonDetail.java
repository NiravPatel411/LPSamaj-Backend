package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

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
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String surname;
    private String profilePic;
    private String email;
    private String gender;
    private String mobileno;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;
}
