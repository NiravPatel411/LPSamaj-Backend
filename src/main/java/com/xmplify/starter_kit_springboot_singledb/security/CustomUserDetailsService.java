package com.xmplify.starter_kit_springboot_singledb.security;

import com.xmplify.starter_kit_springboot_singledb.exception.ResourceNotFoundException;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.AuthAdmin;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrMobileno)
            throws UsernameNotFoundException {

        // Let people login with either username or email
        User user = userRepository.findByMobileno(usernameOrMobileno)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrMobileno)
                );
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AuthAdmin authAdmin = new AuthAdmin();
        List<Admin> admins = adminRepository.isExistsAdminByPerson(user.getId());
        if (Objects.nonNull(admins) && (!"NORMAL".equalsIgnoreCase(request.getAttribute("signInAs").toString()))) {
            if (admins.stream().anyMatch(a -> a.getAdminRole().getName().equalsIgnoreCase(request.getAttribute("signInAs").toString()))) {
                Admin admin = null;
                for (Admin ad : admins) {
                    if (ad.getAdminRole().getName().equalsIgnoreCase(request.getAttribute("signInAs").toString())) {
                        admin = ad;
                        break;
                    }
                }
                authAdmin.setAdminId(admin.getId());
                authAdmin.setAdminName(admin.getName());
                authAdmin.setPersonId(admin.getPerson().getId());
                authAdmin.setAdminType(request.getAttribute("signInAs").toString());
            }
        }

        return UserPrincipal.create(user, authAdmin);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        AuthAdmin authAdmin = new AuthAdmin();

        List<Admin> admins = adminRepository.isExistsAdminByPerson(user.getId());
        if (request.getAttribute("signInAs") != null) {
            if (Objects.nonNull(admins) && (!"NORMAL".equalsIgnoreCase(request.getAttribute("signInAs").toString()))) {
                if (admins.stream().anyMatch(a -> a.getAdminRole().getName().equalsIgnoreCase(request.getAttribute("signInAs").toString()))) {
                    Admin admin = null;
                    for (Admin ad : admins) {
                        if (ad.getAdminRole().getName().equalsIgnoreCase(request.getAttribute("signInAs").toString())) {
                            admin = ad;
                            break;
                        }
                    }
                    authAdmin.setAdminId(admin.getId());
                    authAdmin.setAdminName(admin.getName());
                    authAdmin.setPersonId(admin.getPerson().getId());
                    authAdmin.setAdminType(request.getAttribute("signInAs").toString());
                }
            }
        }
        return UserPrincipal.create(user, authAdmin);
    }
}