package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChangePasswordDTO {

    private String oldPassword;

    private String newPassword;


}
