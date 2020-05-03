package com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EducationDTO {

    private String personEducationId;
    private String personId;

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

    private String isDeleted;

    private String mobileLocalId;

    private String status;
    private String isSync;
}
