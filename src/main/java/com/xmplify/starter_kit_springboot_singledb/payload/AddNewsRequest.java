package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewsRequest {

    @NotNull(message = "newsTypeId can not be null or empty")
    private String newsTypeId;

    @NotNull(message = "title can not be null or empty")
    private String title;

    @NotNull(message = "description can not be null or empty")
    private String description;

    @NotNull(message = "adminId can not be null or empty")
    private String adminId;

    private String extraData;

    private AddNewsMedia[] newsMedia;

}
