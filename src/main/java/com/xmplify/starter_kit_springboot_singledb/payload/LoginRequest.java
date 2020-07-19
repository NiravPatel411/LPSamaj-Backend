package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "usernameOrMobileno can not be blank")
    private String usernameOrMobileno;

    @NotBlank(message = "password can not be blank")
    private String password;

    @NotBlank(message = "signInAs can not be blank")
    private String signInAs;

}
