package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Setter
@Getter
public class AddAdminRequest {

        @NotBlank
        private String personId;

        private List<String> roleType;

}
