package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "working")
@NoArgsConstructor
@Setter
@Getter
public class Working {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "person_id")
    private User person;

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
}
