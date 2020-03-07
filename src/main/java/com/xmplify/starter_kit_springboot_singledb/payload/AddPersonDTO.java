package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
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

    
}