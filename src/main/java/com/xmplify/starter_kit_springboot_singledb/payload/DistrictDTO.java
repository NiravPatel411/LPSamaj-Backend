package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistrictDTO {


    private String name;

    private String stateId;
}
