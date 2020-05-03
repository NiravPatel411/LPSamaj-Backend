package com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class EducationDTO {

    private String personEducationId;
    private String person;

    private String degreeId;

    private String schoolName;

    private String result;

    private String startYear;
    private String endYear;
    private String proofPhoto;
    private String medium;

    private String createdBy;
    private String updatedBy;
    private String deletedBy;

    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private int isDeleted;

    private String mobileLocalId;

    private String status;
}
