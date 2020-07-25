package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.DTOs.person.PersonListDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitteeDTO {
    private String id;
    private String committeeTypeId;
    private String personId;
    private PersonListDTO person;
    private String designation;
}
