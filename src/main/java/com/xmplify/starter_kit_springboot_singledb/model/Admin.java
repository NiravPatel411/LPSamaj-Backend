package com.xmplify.starter_kit_springboot_singledb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "admin", uniqueConstraints = {})
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
    private User person_id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "admin_roles",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "adminRole_id"))
    private Set<AdminRole> adminRoles = new HashSet<>();


}
