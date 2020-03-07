package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jmx.export.annotation.ManagedNotifications;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVillageDTO {

    private String villageId;

    private String villageName;

    private String districtId;
}
