package com.xmplify.starter_kit_springboot_singledb.payload;

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
    private String[] DegreeIds;

    public List<String> getfields() {
        List<String> fields = new ArrayList<>();
        fields.add(GlobalConstants.FIRST_NAME);
        fields.add(GlobalConstants.LAST_NAME);
        fields.add(GlobalConstants.SURNAME);
        fields.add(GlobalConstants.EMAIL);
        fields.add(GlobalConstants.MOBILENO);
        return fields;
    }
}
