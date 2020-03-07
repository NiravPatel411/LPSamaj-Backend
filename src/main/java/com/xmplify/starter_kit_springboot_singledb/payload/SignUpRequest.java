package com.xmplify.starter_kit_springboot_singledb.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.model.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Date;
import java.util.List;
import java.util.Set;

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