package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class AchievementDTO {

    private String title;

    private String description;

    private Date achievementDate;

    private String proofPhoto;

    private String personId;

}
