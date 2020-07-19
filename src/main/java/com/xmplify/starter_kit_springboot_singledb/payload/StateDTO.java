package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StateDTO {

    private String stateName;

    private String CountryId;
}
