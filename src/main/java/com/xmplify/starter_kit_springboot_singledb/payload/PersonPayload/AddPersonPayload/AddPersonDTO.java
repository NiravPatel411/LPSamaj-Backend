package com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.AddPersonPayload;

import com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.EducationDBDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class AddPersonDTO {

    @NotNull
    @Valid
    private PersonDetailDTO personDetail;

    @NotNull
    @Valid
    private List<AddAddressFromUserDTO> address;

    private List<EducationDBDTO> education;
    
}