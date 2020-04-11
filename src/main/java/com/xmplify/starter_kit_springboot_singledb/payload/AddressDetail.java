package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetail {

    private String personAddressId;

    private String personId;

    private String type;

    private String addressText;

    private String countryId;

    private String districtId;
    private String stateId;

    private String deletedBy;

    private String createdBy;

    private String updatedBy;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;

    private String isDelete;

    private String isSync;

}
