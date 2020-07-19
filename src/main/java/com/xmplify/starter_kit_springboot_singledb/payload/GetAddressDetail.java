package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAddressDetail {

    private String id;

    private String personId;

    private String addressType;

    private String addressText;

    private String country;

    private String countryId;

    private String district;

    private String districtId;
    private String stateId;
    private String state;

    private String mobileLocalId;

    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;
}
