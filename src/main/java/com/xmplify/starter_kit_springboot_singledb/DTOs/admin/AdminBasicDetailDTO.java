package com.xmplify.starter_kit_springboot_singledb.DTOs.admin;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.constants.Utility;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminBasicDetailDTO {
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

    private String husbandVillageName;

    private String husbandFirstName;

    private String husbandLastName;

    private String husbandSurname;

    public static List<AdminBasicDetailDTO> create(List<Admin> admins) {
        List<AdminBasicDetailDTO> adminBasicDetailDTOList = new ArrayList<>();
        for(Admin admin : admins){
            adminBasicDetailDTOList.add(AdminBasicDetailDTO.create(admin));
        }
        return adminBasicDetailDTOList;
    }

    public static AdminBasicDetailDTO create(Admin admin) {
        return new AdminBasicDetailDTO(admin.getId(),
                admin.getPerson().getId(),
                admin.getPerson().getFirstName(),
                admin.getPerson().getLastName(),
                admin.getPerson().getSurname(),
                admin.getAdminRole().getName(),
                Objects.nonNull(admin.getPerson().getProfilePic()) ? Utility.fileService.getDeleveryPath(admin.getPerson().getProfilePic(), GlobalConstants.IMAGE, GlobalConstants.PROFILE_EVENT) : "",//TODO : use profile pic utility
                admin.getPerson().getVillage().getName(),
                admin.getPerson().getEmail(),
                admin.getPerson().getGender(),
                admin.getPerson().getMobileno(),
                "",//TODO : Add Husbund village Name
                admin.getPerson().getHusbandFirstName(),
                admin.getPerson().getHusbandLastName(),
                admin.getPerson().getHusbandSurname() //TODO : change LastName to surName
                );
    }
}
