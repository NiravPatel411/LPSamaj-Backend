package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddAddressFromUserDTO {

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

    private String mobileLocalId;

    @NotNull(message = "isSync can not be null or empty")
    private boolean isSync;

    private Timestamp createdDate;

    private Timestamp updatedDate;

    private String createdBy;

    private String updatedBy;

    private int isDeleted;

    private String status;

}
