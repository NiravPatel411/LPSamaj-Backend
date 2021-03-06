package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Address extends AditableEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_Id")
    @JsonIgnore
    private User personId;

    private String addressType;

    private String addressText;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "country_id", insertable = false, updatable = false)
    private String countryId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "district_id", insertable = false, updatable = false)
    private String districtId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @JoinColumn(name = "state_id")
    private State state;

    @Column(name = "state_id", insertable = false, updatable = false)
    private String stateId;

    @Transient
    private String mobileLocalId;

    public static Address create(AddressDTO addressDTO, User savedUser) {
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setPersonId(savedUser);
        address.setAddressType(addressDTO.getAddressType());
        address.setAddressText(addressDTO.getAddressText());
        address.setCountry(new Country(addressDTO.getCountryId()));
        address.setDistrict(new District(addressDTO.getDistrictId()));
        address.setState(new State(addressDTO.getStateId()));
        return address;
    }
}
