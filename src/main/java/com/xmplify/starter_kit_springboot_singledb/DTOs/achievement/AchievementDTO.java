package com.xmplify.starter_kit_springboot_singledb.DTOs.achievement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonalDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.Utility;
import com.xmplify.starter_kit_springboot_singledb.model.PersonAchievement;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class AchievementDTO {

    private String Id;
    private String personId;
    private String achievementTypeId;
    private String description;
    private String achievementDate;
    private MultipartFile proofPhoto;
    private String proofUrl;

    public static List<AchievementDTO> create(List<PersonAchievement> personAchievements) {
        List<AchievementDTO> personalDetailDTOS = new ArrayList<>();
        for(PersonAchievement personAchievement : personAchievements){
            personalDetailDTOS.add(create(personAchievement));
        }
        return personalDetailDTOS;
    }

    public static AchievementDTO create(PersonAchievement personAchievement){
        return new AchievementDTO(
                personAchievement.getId(),
                personAchievement.getPerson().getId(),
                personAchievement.getAchievement().getId(),
                personAchievement.getDescription(),
                personAchievement.getAchievementDate().toString(),
                null,
                Objects.nonNull(personAchievement.getProofPhoto()) ? Utility.fileService.getDeleveryPath(personAchievement.getProofPhoto(), GlobalConstants.IMAGE, GlobalConstants.ACHIEVEMENT_EVENT) : ""
        );
    }

}
