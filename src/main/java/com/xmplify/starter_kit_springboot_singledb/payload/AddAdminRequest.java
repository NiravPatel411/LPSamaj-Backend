package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Setter
@Getter
public class AddAdminRequest {

    @NotBlank
    private String personId;

    private String roleId;

}
