package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VillageDTO {

    private String name;

    private String shortForm;

    private String districtId;
}
