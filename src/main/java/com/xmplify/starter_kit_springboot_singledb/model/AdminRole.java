package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "AdminRole")
@Setter
@Getter
@NoArgsConstructor
public class AdminRole extends AditableEntity{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(length = 60)
    private String name;

    @Column(length = 60)
    private String displayName;
}
