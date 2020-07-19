package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    private int code;
    private Boolean success;
    private String message;
    private Object responseObj;
}
