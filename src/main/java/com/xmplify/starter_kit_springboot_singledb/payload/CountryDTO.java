package com.xmplify.starter_kit_springboot_singledb.payload;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDTO {

    @Column(length = 60)
    private String name;

}
