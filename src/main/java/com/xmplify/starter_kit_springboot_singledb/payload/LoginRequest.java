package com.xmplify.starter_kit_springboot_singledb.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

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
