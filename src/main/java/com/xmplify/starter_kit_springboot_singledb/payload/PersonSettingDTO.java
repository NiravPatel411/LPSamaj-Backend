package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class
PersonSettingDTO {

    private String type;
    private int value;
    private String personId;
}
