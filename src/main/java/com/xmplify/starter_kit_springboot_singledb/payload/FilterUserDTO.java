package com.xmplify.starter_kit_springboot_singledb.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
