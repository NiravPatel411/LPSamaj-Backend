package com.xmplify.starter_kit_springboot_singledb.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank
    private String usernameOrMobileno;

    @NotBlank
    private String password;
}
