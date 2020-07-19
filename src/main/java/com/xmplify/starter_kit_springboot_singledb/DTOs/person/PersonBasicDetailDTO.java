package com.xmplify.starter_kit_springboot_singledb.DTOs.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xmplify.starter_kit_springboot_singledb.DTOs.Address.AddressDTO;
import com.xmplify.starter_kit_springboot_singledb.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.web.ProjectedPayload;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class PersonBasicDetailDTO {
    PersonalDetail personDetail;
    List<AddressDTO> addresses;

    public static PersonBasicDetailDTO create(PersonalDetail personalDetail, List<AddressDTO> returnAddressDTO) {
        return new PersonBasicDetailDTO(personalDetail,returnAddressDTO);
    }
}
