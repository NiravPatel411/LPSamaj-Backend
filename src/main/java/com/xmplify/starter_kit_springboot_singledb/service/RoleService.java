package com.xmplify.starter_kit_springboot_singledb.service;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Role;
import com.xmplify.starter_kit_springboot_singledb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role getNormalUserRole() {
        Optional<Role> userRole = roleRepository.findByNameContainingIgnoreCase(GlobalConstants.ROLE_NORMAL);
        if (!userRole.isPresent()) {
            Role role = new Role();
            role.setDisplayName(GlobalConstants.ROLE_NORMAL);
            role.setName(GlobalConstants.ROLE_NORMAL);
            return roleRepository.save(role);
        } else {
            return userRole.get();
        }
    }


    public Set<Role> toSet(Role normalUserRole) {
        Set<Role> role = new HashSet<>();
        role.add(normalUserRole);
        return role;
    }
}
