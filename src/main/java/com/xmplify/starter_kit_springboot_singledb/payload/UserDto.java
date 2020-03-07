package com.xmplify.starter_kit_springboot_singledb.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.Role;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


	public static UserDto create(User user, String roleType) {
	    System.out.println("user"+user);
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

		UserDto userDto = new UserDto();

			userDto = userDto.builder().id(user.getId()).firstName(user.getFirstName()).email(user.getEmail()).password(user.getPassword()).mobileno(user.getMobileno()).signInAs(roleType).build();
		return userDto;
	}

//	public static UserDto create(Admin admin, String roleType) {
//		System.out.println("user"+admin);
//		List<GrantedAuthority> authorities = admin.getAdminRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//
//		UserDto userDto = new UserDto();
//
//		userDto = userDto.builder().id(admin.getId()).name(admin.getName()).person(admin.getPerson()).signInAs(roleType).build();
//		return userDto;
//	}


}