package com.xmplify.starter_kit_springboot_singledb.controller;
import java.net.URI;
import java.util.*;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.model.AdminRole;
import com.xmplify.starter_kit_springboot_singledb.payload.*;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRoleRepository;
import com.xmplify.starter_kit_springboot_singledb.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.xmplify.starter_kit_springboot_singledb.model.Role;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.repository.RoleRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminRoleRepository adminRoleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrMobileno(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(loginRequest.getRole()))) {
            String jwt = tokenProvider.generateToken(authentication, loginRequest.getRole());
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Map<String, Object> returnUserObject = new HashMap<>();
            if (loginRequest.getSignInAs().equalsIgnoreCase(GlobalConstants.ROLE_NORMAL)) {
                returnUserObject.put("userDetail", UserDto.create(userRepository.findById(userPrincipal.getId()).get(), loginRequest.getSignInAs()));
            }else if(loginRequest.getSignInAs().equalsIgnoreCase(GlobalConstants.ROLE_ADMIN)){
                returnUserObject.put("userDetail", UserDto.create(adminRepository.findById(userPrincipal.getId()).get(), loginRequest.getRole()));
            }
            returnUserObject.put("tokenDetail",new JwtAuthenticationResponse(jwt));
            return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "LOGIN_SUCCESS", returnUserObject), HttpStatus.OK);
        }else {
            return new ResponseEntity(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), true, "UNAUTHORIZED_ACCESS_WITH_ROLE", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
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


}
