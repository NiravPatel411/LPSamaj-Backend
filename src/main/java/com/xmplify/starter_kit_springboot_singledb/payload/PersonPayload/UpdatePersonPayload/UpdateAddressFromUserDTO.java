package com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressFromUserDTO {

    private String addressId;

    @NotNull(message = "addressType can not be null")
    @NotEmpty(message = "addressType can not be empty")
    private String addressType;

    @NotNull(message = "addressText can not be null")
    @NotEmpty(message = "addressText can not be empty")
    private String addressText;

    @NotNull(message = "country can not be null")
    @NotEmpty(message = "country can not be empty")
    private String countryId;

    @NotNull(message = "district can not be null")
    @NotEmpty(message = "district can not be empty")
    private String districtId;

    @NotNull(message = "district can not be null")
    @NotEmpty(message = "district can not be empty")
    private String stateId;

    @NotNull(message = "isSync can not be empty")
    private boolean isSync;

    private String mobileLocalId;

    private String createdDate;

    private String updatedDate;

    private String createdBy;

    private String updatedBy;

    private int isDeleted;

    private String status;
}
