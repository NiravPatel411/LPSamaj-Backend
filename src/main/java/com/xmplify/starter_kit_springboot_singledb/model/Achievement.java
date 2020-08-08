package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "achievement")
@Setter
@Getter
@NoArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(length = 60)
    private String name;

    @Column
    private boolean isProofNedded = false;
}
