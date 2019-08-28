package com.xmplify.starter_kit_springboot_singledb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import com.xmplify.starter_kit_springboot_singledb.model.Role;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRoleRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.RoleRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminRoleRepository adminRoleRepository;

    @PostMapping("/add")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userRepository.existsByMobileno(signUpRequest.getMobileno())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false, "Mobileno already in use!",null),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getSurname(),
                signUpRequest.getProfilePic(),
                signUpRequest.getVillageId(),
                signUpRequest.getEmail(),
                signUpRequest.getGender(),
                signUpRequest.getBirthDate(),
                signUpRequest.getBloodGroup(),
                signUpRequest.getMaritualStatus(),
                signUpRequest.getCurrentAddress(),
                signUpRequest.getPermenentAddress(),
                signUpRequest.getPassword(),
                signUpRequest.getMobileno());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> userRole = roleRepository.findByNameIn(signUpRequest.getRoleType());
        Set<Role> userRoleSet = new HashSet<Role>(userRole);
        user.setRoles(userRoleSet);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getFirstName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(HttpStatus.OK.value(),true, "User registered successfully",null));
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<?> addAdmin(@Valid @RequestBody AddAdminRequest addAdminRequest) {
        Admin result = null;
        if(!userRepository.existsById(addAdminRequest.getPersonId())){
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false, "Can not find person by person id",null),
                    HttpStatus.BAD_REQUEST);
        }

        Admin admin = new Admin();
        Optional<User> person = userRepository.findById(addAdminRequest.getPersonId());
        if(person.isPresent()){
            admin.setName(person.get().getFirstName()+" "+person.get().getLastName());
            admin.setPerson_id(person.get());
            List<AdminRole> userRole = adminRoleRepository.findByNameIn(addAdminRequest.getRoleType());
            Set<AdminRole> userRoleSet = new HashSet<AdminRole>(userRole);
            admin.setAdminRoles(userRoleSet);
            result = adminRepository.save(admin);
        } else{
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Person Details Not Found", result), HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Admin Created", result), HttpStatus.OK);
    }
}
