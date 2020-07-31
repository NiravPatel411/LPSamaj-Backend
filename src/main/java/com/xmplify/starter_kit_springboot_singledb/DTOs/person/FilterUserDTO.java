package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserDTO {

    String searchText;
    private String[] villageIds;
    private String gender;
    private Long greaterThanBOD;
    private Long lessThanBOD;
    private String maritalStatus;
    private boolean bloodDonor;
    private String[] bloodGroup;
    private String[] DegreeIds;

    public List<String> getFreeTextfields() {
        List<String> fields = new ArrayList<>();
        fields.add(GlobalConstants.FIRST_NAME_FIELD);
        fields.add(GlobalConstants.LAST_NAME_FIELD);
        fields.add(GlobalConstants.SURNAME_FIELD);
        fields.add(GlobalConstants.EMAIL_FIELD);
        fields.add(GlobalConstants.MOBILENO_FIELD);

        return fields;
    }
}
