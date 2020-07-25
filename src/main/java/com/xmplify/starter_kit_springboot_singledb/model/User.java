package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.DTOs.PersonBasicDetailDTO1;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetail;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.Utility;
import com.xmplify.starter_kit_springboot_singledb.security.SecurityUtils;
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
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "userName","email"
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

    private String familyCode;

    private String userName;

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

    @Column(name = "village_id", insertable = false, updatable = false)
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

    @JsonFormat(pattern = "dd-MM-yyyy")
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "person")
    private List<PersonAchievement> achievements;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "person")
    private List<PersonEducation> educations;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "person")
    private Set<PersonWorking> personWorkings;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "personSetting_Id")
    private PersonSetting personSetting;

    @OneToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Admin admin;

    @Column(name = "admin_id", insertable = false, updatable = false)
    private String adminId;

    @OneToMany(mappedBy = "personId")
    private List<Address> addressList;

    @Column(name = "hobby")
    private String hobby;

    public User(String id, String familyCode, String userName, String firstName, String lastName, String surname,
                String profileURL, Village village, String villageId, String husbandVillageId, String husbandFirstName,
                String husbandLastName, String husbandSurname, String email, String gender, LocalDate birthDate,
                String bloodGroup, String maritalStatus, String password, String mobileno, String adminId, String hobby) {
        this.id = id;
        this.familyCode = familyCode;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.surname = surname;
        this.profilePic = profileURL;
        this.village = village;
        this.villageId = villageId;
        this.husbandVillageId = husbandVillageId;
        this.husbandFirstName = husbandFirstName;
        this.husbandLastName = husbandLastName;
        this.husbandSurname = husbandSurname;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.bloodGroup = bloodGroup;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.mobileno = mobileno;
        this.hobby=hobby;
        this.admin = new Admin(adminId);

    }

    public static User create(PersonalDetail personalDetail, Set<Role> userRole, PersonSetting savedSetting) {
        User user =  new User(personalDetail.getId(),personalDetail.getFamilyCode(),
                personalDetail.getUserName(),
                personalDetail.getFirstName(),
                personalDetail.getLastName(),
                personalDetail.getSurname(),
                personalDetail.getProfileURL(),
                new Village(personalDetail.getVillageId()),
                personalDetail.getVillageId(),
                personalDetail.getHusbandVillageId(),
                personalDetail.getHusbandFirstName(),
                personalDetail.getHusbandLastName(),
                personalDetail.getHusbandSurname(),
                personalDetail.getEmail(),
                personalDetail.getGender(),
                LocalDate.parse(personalDetail.getBirthDate(), GlobalConstants.DATE_FORMAT),
                personalDetail.getBloodGroup(),
                personalDetail.getMaritalStatus(),
                Utility.passwordEncoder.encode(personalDetail.getPassword()),
                personalDetail.getMobileno(),
                personalDetail.getAdminId(),
                personalDetail.getHobby()
                );
        user.setRoles(userRole);
        user.setPersonSetting(savedSetting);
        return user;
    }
}