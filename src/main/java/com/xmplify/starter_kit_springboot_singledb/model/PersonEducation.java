package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.DTOs.education.EducationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personEducation")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PersonEducation extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String personEducationId;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private User person;

    private String degreeId;

    private String schoolName;

    private String result;

    private String startYear;
    private String endYear;
    private String proofPhoto;
    private String medium;



    public static PersonEducation create(EducationDTO educationDTO) {
        return new PersonEducation(educationDTO.getId(),new User(educationDTO.getPersonId()),educationDTO.getDegreeId(),
                educationDTO.getSchoolName(),
                educationDTO.getResult(),
                educationDTO.getStartYear(),
                educationDTO.getEndYear(),
                educationDTO.getProofPhoto(),
                educationDTO.getMedium());
    }

    public static List<PersonEducation> create(List<EducationDTO> educationDTOList){
        List<PersonEducation> personEducationList = new ArrayList<>();
        for(EducationDTO educationDTO : educationDTOList){
            personEducationList.add(create(educationDTO));
        }
        return personEducationList;
    }
}
