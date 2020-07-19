package com.xmplify.starter_kit_springboot_singledb.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserDto {

    private String id;

    private String name;

    private String firstName;

    private String email;

    private String password;

    private String mobileno;

    private String signInAs;

    private User person;

    private PersonSettingResponseDTO personSetting;


    public static UserDto create(User user, String roleType) {
        System.out.println("user" + user);
        PersonSettingResponseDTO personSettingResponseDTO = PersonSettingResponseDTO.toDTO(user.getPersonSetting());
        UserDto userDto = new UserDto();
        userDto = builder().id(user.getId()).firstName(user.getFirstName()).email(user.getEmail()).password(user.getPassword()).mobileno(user.getMobileno()).signInAs(roleType).personSetting(personSettingResponseDTO).build();
        return userDto;
    }

}