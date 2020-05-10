package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "mobileno"
        })
})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User extends AditableEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String surname;

    private String profilePic;

    @JoinColumn(name = "village_id")
    @OneToOne
    @JsonIgnore
    private Village village;

    @Column(name ="village_id",insertable = false,updatable = false)
    private String villageId;

    private String husbandVillageId;

    @Size(max = 50)
    private String husbandFirstName;

    @Size(max = 50)
    private String husbandLastName;

    @Size(max = 50)
    private String husbandSurname;


    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    private String gender;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate birthDate;

    private String bloodGroup;

    private String maritalStatus;

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
    private List<PersonAchievement> achievements;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "person")
    private List<PersonEducation> educations;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "person")
    private Set<PersonWorking> personWorkings;

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

}