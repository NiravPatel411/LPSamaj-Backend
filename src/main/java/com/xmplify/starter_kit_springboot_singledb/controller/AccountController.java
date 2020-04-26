package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.model.Account;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.account.AccountDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.AccountRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AdminRepository adminRepository;


    @GetMapping("/")
    public ResponseEntity<?> getAllAccount() {
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", accountRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAccountData(@RequestBody AccountDTO accountDTO) {

        Optional<Admin> admin = adminRepository.findById(accountDTO.getAdminId());

        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Your are not authorized to add the data", null), HttpStatus.BAD_REQUEST);
        }


        Account account = new Account();
        account.setAmount(accountDTO.getAmount());
        account.setDate(accountDTO.getDate());
        account.setReason(accountDTO.getReason());
        account.setType(accountDTO.getType());
        account.setCreatedBy(admin.get());
        account.setUpdatedBy(admin.get());

        Object obj = accountRepository.save(account);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "added sucessfilly", ""), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDTO accountDTO) {


        Optional<Admin> admin = adminRepository.findById(accountDTO.getAdminId());

        if (!admin.isPresent()) {
            return new ResponseEntity(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "Your are not authorized to add the data", null), HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAmount(accountDTO.getAmount());
        account.setDate(accountDTO.getDate());
        account.setReason(accountDTO.getReason());
        account.setType(accountDTO.getType());
        account.setUpdatedBy(admin.get());

        Object obj = accountRepository.save(account);
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "updated successfully", ""), HttpStatus.OK);
    }
}
