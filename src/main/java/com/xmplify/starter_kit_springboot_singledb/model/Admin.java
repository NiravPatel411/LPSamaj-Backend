package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "admin")
@NoArgsConstructor
@Setter
@Getter
public class Admin implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Size(max = 40)
    private String name;

    @OneToOne
    private User person;

    @OneToOne
    private AdminRole adminRole;

    public Admin(String adminId) {
        this.id = adminId;
    }
}
