package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonDetailDTO {

    private String personId;

    @NotNull(message = "firstName can not be null or empty")
    private String firstName;

    @NotNull(message = "lastName can not be null or empty")
    private String lastName;

    @NotNull(message = "surname can not be null or empty")
    private String surname;

    @NotNull(message = "profilePic can not be null or empty")
    private String profilePic;

    @NotNull(message = "villageId can not be null or empty")
    private String villageId;

    @NotNull(message = "email can not be null or empty")
    private String email;

    @NotNull(message = "gender can not be null or empty")
    private String gender;

    @NotNull(message = "birthDate can not be null or empty")
    private String birthDate;

    private String bloodGroup;

    @NotNull(message = "maritualStatus can not be null or empty")
    private String maritualStatus;

    @NotNull(message = "password can not be null or empty")
    private String password;

    @NotNull(message = "mobileno can not be null or empty")
    private String mobileno;

    @NotNull(message = "gender can not be null or empty")
    private String adminId;

    @NotNull(message = "isSync can not be null or empty")
    private boolean isSync;

    private String mobileLocalId;

    private String createdDate;

    private String updatedDate;

    @NotNull(message = "createdBy can not be null")
    private String createdBy;

    private String updatedBy;

    private int isDeleted;

    private String status;
}
