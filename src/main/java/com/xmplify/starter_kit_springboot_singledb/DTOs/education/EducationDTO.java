package com.xmplify.starter_kit_springboot_singledb.DTOs.education;

import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail;
import com.xmplify.starter_kit_springboot_singledb.model.PersonEducation;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationDTO {
    private String id;
    private String personId;
    private String degreeId;
    private String schoolName;
    private String result;
    private String startYear;
    private String endYear;
    private String proofPhoto;
    private String medium;
    private String createdBy;
    private String CreatedByName;
    private String updatedBy;
    private String updatedByName;
    private String createdAt;
    private String updatedAt;
    private String isDeleted;
    private String status;

    public static EducationDTO create(PersonEducation personEducation){
        return new EducationDTO(personEducation.getPersonEducationId(),
                personEducation.getPerson().getId(),
                personEducation.getDegreeId(),
                personEducation.getSchoolName(),
                personEducation.getResult(),
                personEducation.getStartYear(),
                personEducation.getEndYear(),
                personEducation.getProofPhoto(),
                personEducation.getMedium(),
                Objects.nonNull(personEducation.getCreatedBy()) ? personEducation.getCreatedBy().getId() : "",
                Objects.nonNull(personEducation.getCreatedBy()) ? personEducation.getCreatedBy().getName() : "",
                Objects.nonNull(personEducation.getUpdatedBy()) ? personEducation.getUpdatedBy().getId() : "",
                Objects.nonNull(personEducation.getUpdatedBy()) ? personEducation.getUpdatedBy().getName() : "",
                Objects.nonNull(personEducation.getCreatedAt()) ? personEducation.getCreatedAt().toString() : "",
                Objects.nonNull(personEducation.getUpdatedAt()) ? personEducation.getUpdatedAt().toString() : "",
                String.valueOf(personEducation.getIsDeleted()),
                personEducation.getStatus());
    }

    public static List<EducationDTO> create(List<PersonEducation> personEducations) {
        List<EducationDTO> educationDTOS = new ArrayList<>();
        for(PersonEducation personEducation : personEducations){
            educationDTOS.add(create(personEducation));
        }
        return educationDTOS;
    }
}
