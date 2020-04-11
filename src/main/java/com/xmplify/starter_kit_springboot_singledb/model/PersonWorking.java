package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "personWorking")
@NoArgsConstructor
@Setter
@Getter
public class PersonWorking extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String personWorkingId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private User person;

    private String organizationName;

    private Date startDate;

    private Date endDate;

    private String position;

    private String proofPhoto;

    private String address;

    private String description;

    private String occuption; //
    private String sector; // private ,Goverment ,Personal business
    private String classType; // for gov
    private String workingAddress; // for gov
    private String businessDescription; // for gov
    private String startWorkingYear;
    private String endWorkingYear;
}
