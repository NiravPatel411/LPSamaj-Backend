package com.xmplify.starter_kit_springboot_singledb.model;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "mobileno"
        })
})
@NoArgsConstructor
@Setter
@Getter
public class User extends AditableEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Size(max = 15)
    private String firstName;

    @Size(max = 15)
    private String lastName;

    @Size(max = 15)
    private String surname;

    private String profilePic;

    private String villageId;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    private String gender;

    private Date birthDate;

    private String bloodGroup;

    private String maritualStatus;

    private String currentAddress;

    private String permenentAddress;

    @JsonIgnore
    @Size(max = 100)
    private String password;
    
    @Size(max = 15)
    private String mobileno;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String name, String email, String password, String mobileno) {
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
    }

    public User( @Size(max = 15) String firstName, @Size(max = 15) String lastName, @Size(max = 15) String surname, String profilePic, String villageId, @Size(max = 40) @Email String email, String gender, Date birthDate, String bloodGroup, String maritualStatus, String currentAddress, String permenentAddress, @Size(max = 100) String password, @Size(max = 15) String mobileno) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.profilePic = profilePic;
        this.villageId = villageId;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
        this.maritualStatus = maritualStatus;
        this.currentAddress = currentAddress;
        this.permenentAddress = permenentAddress;
        this.password = password;
        this.mobileno = mobileno;
    }
}