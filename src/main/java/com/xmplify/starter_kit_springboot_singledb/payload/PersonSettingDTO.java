package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class
PersonSettingDTO {

    private String id;
    private String type;
    private int value;
}
