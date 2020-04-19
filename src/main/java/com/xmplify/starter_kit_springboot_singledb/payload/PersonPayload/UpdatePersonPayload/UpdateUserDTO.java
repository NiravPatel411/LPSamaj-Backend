package com.xmplify.starter_kit_springboot_singledb.payload.PersonPayload.UpdatePersonPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @NotNull(message = "person detail can not be null")
    @Valid
    private UpdatePersonDetailDTO personDetail;

    @NotNull (message = "address can not be null")
    @Valid
    private List<UpdateAddressFromUserDTO> address;

}
