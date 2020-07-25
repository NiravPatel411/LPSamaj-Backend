package com.xmplify.starter_kit_springboot_singledb.DTOs.Address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {

    private String id;
    private String personId;
    private String addressType;
    private String addressText;
    private String countryName;
    private String countryId;
    private String districtName;
    private String districtId;
    private String stateName;
    private String stateId;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String createdByName;
    private String updatedBy;
    private String updatedByName;
    private int isDeleted;
    private String status;

    public static AddressDTO create(Address address) {
        return new AddressDTO(address.getId(),
                address.getPersonId().getId(),
                address.getAddressType(),
                address.getAddressText(),
                address.getCountry().getName(),
                address.getCountry().getId(),
                address.getDistrict().getName(),
                address.getDistrict().getId(),
                address.getState().getName(),
                address.getState().getId(),
                Objects.nonNull(address.getCreatedAt()) ? address.getCreatedAt().toString() : "",
                Objects.nonNull(address.getUpdatedAt()) ? address.getUpdatedAt().toString() : "",
                Objects.nonNull(address.getCreatedBy()) ? address.getCreatedBy().getId() : "",
                Objects.nonNull(address.getCreatedBy()) ? address.getCreatedBy().getName() : "",
                Objects.nonNull(address.getUpdatedBy()) ? address.getUpdatedBy().getId() : "",
                Objects.nonNull(address.getUpdatedBy()) ? address.getUpdatedBy().getName() : "",
                address.getIsDeleted(),
                address.getStatus()
                );
    }
}
