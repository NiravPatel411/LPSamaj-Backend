package com.xmplify.starter_kit_springboot_singledb.payload;

import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBasicDetail {
    private String adminId;
    private String personId;
    private String firstName;
    private String lastName;
    private String surname;
    private String adminType;
    private String profilePic;
    private String villageName;
    private String email;
    private String gender;
    private String mobileno;

}
