package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VillageDTO {

    private String name;

    private String districtId;
}
