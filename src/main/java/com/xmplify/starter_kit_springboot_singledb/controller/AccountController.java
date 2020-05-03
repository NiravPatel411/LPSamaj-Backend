package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.model.Account;
import com.xmplify.starter_kit_springboot_singledb.model.Admin;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.account.AccountDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.account.AccountDetailDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.account.AccountYearDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.AccountRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

        List<Account> accountList = accountRepository.findAll();
        List<AccountDTO> responseList = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(accountList.get(i).getId());
            accountDTO.setAmount(accountList.get(i).getAmount());
            accountDTO.setType(accountList.get(i).getType());
            accountDTO.setDate(accountList.get(i).getDate());
            accountDTO.setReason(accountList.get(i).getReason());
            responseList.add(accountDTO);
        }


        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", responseList), HttpStatus.OK);
    }

    @GetMapping("/accountYearList")
    public ResponseEntity<?> getAllAccountYearList() {
        List<String> listOfYear = accountRepository.getListOfYear();
        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", listOfYear), HttpStatus.OK);
    }

    @PostMapping("/accountByYear")
    public ResponseEntity<?> getAcccountByYear(@RequestBody AccountYearDTO yearDTO) {
        List<Account> accountList = accountRepository.getAccountByYear(yearDTO.getYear());

        AccountDetailDTO detailDTO = new AccountDetailDTO();

        List<AccountDTO> responseList = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(accountList.get(i).getId());
            accountDTO.setAmount(accountList.get(i).getAmount());
            accountDTO.setType(accountList.get(i).getType());
            accountDTO.setDate(accountList.get(i).getDate());
            accountDTO.setReason(accountList.get(i).getReason());
            responseList.add(accountDTO);
        }
        detailDTO.setAccount(responseList);

        List<Account> previousYearAcountList = accountRepository.getAccountByBeforeYear(yearDTO.getYear());
        int previousYearAcountRemaining = 0;
        for (int i = 0; i < previousYearAcountList.size(); i++) {
            if (previousYearAcountList.get(i).getType().equalsIgnoreCase(GlobalConstants.DEBIT)) {
                previousYearAcountRemaining = previousYearAcountRemaining - previousYearAcountList.get(i).getAmount();
            } else {
                previousYearAcountRemaining = previousYearAcountRemaining + previousYearAcountList.get(i).getAmount();
            }
        }
        detailDTO.setRemainingAmountBeforeYear(String.valueOf(previousYearAcountRemaining));


        return new ResponseEntity(new ApiResponse(HttpStatus.OK.value(), true, "SUCCESS", detailDTO), HttpStatus.OK);
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
