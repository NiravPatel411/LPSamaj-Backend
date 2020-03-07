package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdmin {

    @NotEmpty(message = "adminId can not be empty or null")
    private String adminId;

    @NotEmpty(message = "personId can not be empty or null")
    private String personId;

}
