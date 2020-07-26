package com.xmplify.starter_kit_springboot_singledb.DTOs.education;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationListDTO {

    List<EducationDTO> education;
}
