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
@NoArgsConstructor
@ToString
public class EducationDTO {

    private String personEducationId;
    private User person;

    private String degreeId;

    private String schoolName;

    private String result;

    private String startDate;

    private String endDate;

    private String degreeName;
    private String startYear;
    private String endYear;
    private String proofPhoto;
    private String medium;

    private String createdBy;
    private String updatedBy;
    private String deletedBy;

    private String createdAt;
    private String updatedAt;
    private Date deletedAt;

    private int isDeleted;

    private String status;
}
