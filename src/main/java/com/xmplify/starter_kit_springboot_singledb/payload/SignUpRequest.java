package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class SignUpRequest {

    private String firstName;

    private String lastName;

    private String surname;

    private String profilePic;

    private String villageId;

    private String email;

    private String gender;

    private Date birthDate;

    private String bloodGroup;

    private String maritualStatus;

    private String currentAddress;

    private String permenentAddress;

    private String password;

    private String mobileno;

    private List<String> roleType;

    private String adminId;

}