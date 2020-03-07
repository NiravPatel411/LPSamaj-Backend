package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class WorkingDTO {

    private String occuption;

    private String sector;

    private String organizationName;

    private Date startDate;

    private Date endDate;

    private String position;

    private String proofPhoto;

    private String class_type;

    private String address;

    private String description;

    private String personId;
}
