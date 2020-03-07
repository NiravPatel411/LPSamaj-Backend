package com.xmplify.starter_kit_springboot_singledb.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistrictListDTO {

    private String districtId;
    private String districtName;
    private String stateId;
    private String stateName;
    private String countryId;
    private String countryName;
}
