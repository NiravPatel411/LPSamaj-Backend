package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "personEducation")
@NoArgsConstructor
@Setter
@Getter
public class PersonEducation extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String personEducationId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private User person;

    private String degreeId;

    private String schoolName;

    private String result;

    private String startDate;

    private String endDate;
//

    private String degreeName;
    private String startYear;
    private String endYear;
    private String proofPhoto;
    private String medium;

}
