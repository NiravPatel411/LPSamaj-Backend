package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate birthDate;
    private String firstName;
    private String lastName;
    private String surname;
    private String profilePic;
    private String email;
    private String gender;
    private String mobileno;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;
}
