package com.xmplify.starter_kit_springboot_singledb.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xmplify.starter_kit_springboot_singledb.model.Country;
import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

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

    private String mobileLocalId;

    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String createdBy;
    private String updatedBy;
    private int isDeleted;
    private String status;
}
