package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.CommitteeMapper;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeType;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeTypeRepository;
import com.xmplify.starter_kit_springboot_singledb.service.impl.CommitteeService;
import com.xmplify.starter_kit_springboot_singledb.service.impl.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/committee")
public class CommitteeController {

    private final CommitteeRepository committeeRepository;

    private final
    CommitteeService committeeService;

    private final
    CommitteeMapper committeeMapper;

    private final
    Validators validators;

    private final
    CommitteeTypeRepository committeeTypeRepository;

    @Autowired
    public CommitteeController(CommitteeRepository committeeRepository, CommitteeService committeeService, CommitteeMapper committeeMapper, Validators validators, CommitteeTypeRepository committeeTypeRepository) {
        this.committeeRepository = committeeRepository;
        this.committeeService = committeeService;
        this.committeeMapper = committeeMapper;
        this.validators = validators;
        this.committeeTypeRepository = committeeTypeRepository;
    }

    @PostMapping("/addUpdateCommittee")
    public ResponseEntity<?> addUpdateCommittee(@RequestBody CommitteeDTO committeeDTO){
        if(!Objects.nonNull(committeeDTO)){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "RequestBody not found"), HttpStatus.BAD_REQUEST);
        }
        if(Objects.nonNull(committeeDTO.getId())){
            List<String> messages = validators.validateUpdateCommitteeDTO(committeeDTO);
            if(!messages.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Updated",  committeeService.AddUpdateCommittee(committeeDTO)), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddCommitteeDTO(committeeDTO);
            if(!messages.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Added", committeeService.AddUpdateCommittee(committeeDTO)), HttpStatus.OK);

        }
    }

    @PostMapping("/addCommitteeType")
    public ResponseEntity<?> addCommitteeType(@RequestBody CommitteeType committeeType){
        if(Objects.isNull(committeeType)){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "RequestBody not found"), HttpStatus.BAD_REQUEST);
        }

        if(Objects.nonNull(committeeType.getId())){
            List<String> messages = validators.validateUpdateCommitteeType(committeeType);
            if(!messages.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddCommitteeType(committeeType);
            if(!messages.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        }
    }

    @GetMapping("/committeeType")
    public ResponseEntity<?> getAllCommitteeType(){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Success", committeeTypeRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/committeeByType/{type}")
    public ResponseEntity<?> getCommitteeByType(@PathVariable String type,@PageableDefault(page = 0,size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable){
        Page<CommitteeMember> committeeMemberPage =  committeeRepository.getCommitteeMemberByCommitteeTypeId(type,pageable);
        List<CommitteeDTO> committeeDTOS = new ArrayList<>();
        for(CommitteeMember committeeMembers : committeeMemberPage.getContent()){
            CommitteeDTO committeeDTO = new CommitteeDTO(committeeMembers.getId(),
                    committeeMembers.getCommitteeType().getId(),
                    committeeMembers.getUserId().getId(),
                    committeeMembers.getDesignation());
            committeeDTOS.add(committeeDTO);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), false, "Success", committeeDTOS), HttpStatus.OK);
    }

}
