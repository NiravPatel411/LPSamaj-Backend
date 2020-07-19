package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class News extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @OneToOne
    private NewsType newsType;

    private String title;
    @Column(columnDefinition = "Text")
    private String description;

    @Column(columnDefinition = "Text")
    private String extraData;

    @OneToOne
    private Admin adminId;


}
