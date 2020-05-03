package com.xmplify.starter_kit_springboot_singledb.controller;
import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.setAttribute("signInAs",loginRequest.getSignInAs());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrMobileno(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication, loginRequest.getSignInAs());
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> returnUserObject = new HashMap<>();
        List<Admin> admins = adminRepository.isExistsAdminByPerson(userPrincipal.getId());
        if(Objects.nonNull(admins) && (! "NORMAL".equalsIgnoreCase(loginRequest.getSignInAs()))){
            if(admins.stream().anyMatch(a -> a.getAdminRole().getName().equalsIgnoreCase(loginRequest.getSignInAs()))){
                returnUserObject.put("userDetail", UserDto.create(userRepository.findById(userPrincipal.getId()).get(), loginRequest.getSignInAs()));
                AuthAdmin authAdmin = new AuthAdmin();
                Admin admin = null;
                for(Admin ad : admins){
                    if(ad.getAdminRole().getName().equalsIgnoreCase(loginRequest.getSignInAs())){
                        admin = ad;
                        break;
                    }
                }
                if(Objects.isNull(admin)){
                    return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "NOT ACCESS with "+loginRequest.getSignInAs()+" role",null), HttpStatus.OK);
                }
                authAdmin.setAdminId(admin.getId());
                authAdmin.setAdminName(admin.getName());
                authAdmin.setPersonId(admin.getPerson().getId());
                authAdmin.setAdminType(loginRequest.getSignInAs());

                returnUserObject.put("adminDetail",authAdmin);
            }else {
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "NOT ACCESS with "+loginRequest.getSignInAs()+" role",null), HttpStatus.OK);
            }
        } else {
            if("NORMAL".equalsIgnoreCase(loginRequest.getSignInAs())) {
                returnUserObject.put("userDetail", UserDto.create(userRepository.findById(userPrincipal.getId()).get(), loginRequest.getSignInAs()));
            }else {
                return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "NOT ACCESS with "+loginRequest.getSignInAs()+" role",null), HttpStatus.OK);
            }
        }
        returnUserObject.put("tokenDetail",new JwtAuthenticationResponse(jwt));
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "LOGIN_SUCCESS", returnUserObject), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if(userRepository.existsByMobileno(signUpRequest.getMobileno())) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(),false, "Mobileno already in use!",null),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setMobileno(signUpRequest.getMobileno());
        user.setPassword(signUpRequest.getPassword());
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

    @PostMapping("/addRole")
    public ResponseEntity<?> addRole(@RequestBody List<String> role){
        List<Role> roles = new ArrayList<>();
        role.stream().forEach((name) -> {
            Role obj = new Role();
            obj.setName(name);
            obj.setDisplayName(name);
            roles.add(obj);
        });
        Object ret = roleRepository.saveAll(roles);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", ret), HttpStatus.OK);
    }

    @PostMapping("/addAdminRole")
    public ResponseEntity<?> addAdminRole(@RequestBody List<String> role){
        List<AdminRole> roles = new ArrayList<>();
        role.stream().forEach((name) -> {
            AdminRole obj = new AdminRole();
            obj.setName(name);
            obj.setDisplayName(name);
            roles.add(obj);
        });
        Object ret = adminRoleRepository.saveAll(roles);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", ret), HttpStatus.OK);
    }

    @GetMapping("/adminTypes")
    public ResponseEntity<?> getAllAdminTypes(){
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "Role Added", adminRoleRepository.findAll()), HttpStatus.OK);
    }
}