package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonAllDetails {

    private PersonalDetail personalDetails;
    private List<AddressDetail> addressDetails;
    private List<EducationDTO> educationDetails;
}
