package com.xmplify.starter_kit_springboot_singledb.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xmplify.starter_kit_springboot_singledb.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/list")
    public ResponseEntity<?> listUser() {
    	
    	

        return ResponseEntity.ok(userService.findAll());
    }
}
