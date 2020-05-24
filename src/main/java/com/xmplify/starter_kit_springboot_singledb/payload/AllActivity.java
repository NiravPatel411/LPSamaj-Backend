package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllActivity {
    private String id;

    private String aim;

    private String description;

    private String dateTime;

    private String conclusion;
    private String place;

    private String adminId;

    private String adminFirstName;

    private String adminLastName;

    private String adminSurname;

    private String extraData;

    private String createdAt;

    private String updatedAt;

    private List<AllMedia> allMedia;
}
