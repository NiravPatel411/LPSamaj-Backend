package com.xmplify.starter_kit_springboot_singledb.payload.activity;

import com.xmplify.starter_kit_springboot_singledb.payload.AddEditMedia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddActivityRequest {

    @NotNull(message = "Aim can not be null or empty")
    private String aim;
    @NotNull(message = "Description can not be null or empty")
    private String description;
    @NotNull(message = "Date time can not be null or empty")
    private String dateTime;
    @NotNull(message = "Conclusion can not be null or empty")
    private String conclusion;

    @NotNull(message = "adminId can not be null or empty")
    private String adminId;

    private String id;

    private AddEditMedia[] newsMedia;

    private String extraData;
    private String deletedMediaIds;
}
