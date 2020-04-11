package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressDTO {

    private String addressId;

    private String personId;

    private String addressType;

    private String addressText;

    private String countryId;

    private String districtId;
    private String stateId;
}
