package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter@AllArgsConstructor
@NoArgsConstructor
public class AddNewsType {

    @NotNull(message = "Name can not be null or empty")
    private String Name;
    @NotNull
    private int priority_number;
}
