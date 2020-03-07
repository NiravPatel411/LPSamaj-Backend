package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class EducationDTO {

    private String degreeId;

    private String schoolName;

    private String result;

    private String startDate;

    private String endDate;

    private String PersonId;
}
