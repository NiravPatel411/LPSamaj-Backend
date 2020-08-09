package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.DTOs.achievement.AchievementDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "personAchievement")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PersonAchievement extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    private User person;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JsonIgnore
    private Achievement achievement;

    private String description;

    private LocalDate achievementDate;

    private String proofPhoto;

    public PersonAchievement(String id, User user, Achievement achievement, String description, LocalDate achievementDate) {
        this.id = id;
        this.person = user;
        this.achievement = achievement;
        this.description = description;
        this.achievementDate = achievementDate;
    }

    public static PersonAchievement create(AchievementDTO achievementDTO) {
        return new PersonAchievement(
                achievementDTO.getId(),
                new User(achievementDTO.getPersonId()),
                new Achievement(achievementDTO.getAchievementTypeId()),
                achievementDTO.getDescription(),
                LocalDate.parse(achievementDTO.getAchievementDate(), GlobalConstants.DATE_FORMAT)
        );
    }
}
