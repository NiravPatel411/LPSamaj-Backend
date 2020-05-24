package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Activity extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(columnDefinition = "Text")
    private String description;

    @Column(columnDefinition = "Text")
    private String extraData;

    @Column(columnDefinition = "Text")
    private String aim;

    private String dateTime;

    private String conclusion;
    private String place;

    @OneToOne
    private Admin adminId;


}
