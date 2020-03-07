package com.xmplify.starter_kit_springboot_singledb.model;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
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

    @JoinColumn(name = "village_id")
    @OneToOne
    @JsonIgnore
    private Village village;

    @Column(name ="village_id",insertable = false,updatable = false)
    private String villageId;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    private String gender;

    private Date birthDate;

    private String bloodGroup;

    private String maritualStatus;

    @JsonIgnore
    @Size(max = 100)
    private String password;
    
    @Size(max = 15)
    private String mobileno;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "person")
    private Set<Achievement> achievements;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "person")
    private Set<Education> educations;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "person")
    private Set<Working> workings;

    @OneToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @Column(name ="admin_id",insertable = false,updatable = false)
    private String adminId;

    @Transient
    private String mobileLocalId;

    @OneToMany(mappedBy = "personId")
    private List<Address> addressList;


    public User(String name, String email, String password, String mobileno) {
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
    }

    public User( @Size(max = 15) String firstName, @Size(max = 15) String lastName, @Size(max = 15) String surname, String profilePic, @Size(max = 40) @Email String email, String gender, Date birthDate, String bloodGroup, String maritualStatus, @Size(max = 100) String password, @Size(max = 15) String mobileno) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.profilePic = profilePic;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
        this.maritualStatus = maritualStatus;
        this.password = password;
        this.mobileno = mobileno;
    }
}