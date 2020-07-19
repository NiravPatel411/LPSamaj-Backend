package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "media")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Media {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String mediaType;

    private String relatedType;

    private String relatedId;

    private String storePath;

    private String createdAt;

    private int idDeleted = 0;

}
