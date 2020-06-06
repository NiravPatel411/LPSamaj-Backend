package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.CommitteeMapper;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeType;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.payload.ListPersonBasicDetail;
import com.xmplify.starter_kit_springboot_singledb.payload.PersonAllDetails;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeTypeRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
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

    private final UserService userService;

    private final
    Validators validators;

    private final
    CommitteeTypeRepository committeeTypeRepository;

    final
    UserRepository userRepository;

    @Autowired
    public CommitteeController(CommitteeRepository committeeRepository, CommitteeService committeeService, CommitteeMapper committeeMapper, UserService userService, Validators validators, CommitteeTypeRepository committeeTypeRepository, UserRepository userRepository) {
        this.committeeRepository = committeeRepository;
        this.committeeService = committeeService;
        this.committeeMapper = committeeMapper;
        this.userService = userService;
        this.validators = validators;
        this.committeeTypeRepository = committeeTypeRepository;
        this.userRepository = userRepository;
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
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddCommitteeType(committeeType);
            if(!messages.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        }
    }

    @GetMapping("/committeeType")
    public ResponseEntity<?> getAllCommitteeType(){
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", committeeTypeRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/committeeByType/{type}")
    public ResponseEntity<?> getCommitteeByType(@PathVariable String type,@PageableDefault(page = 0,size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable){
        Page<CommitteeMember> committeeMemberPage =  committeeRepository.getCommitteeMemberByCommitteeTypeId(type,pageable);
        List<CommitteeDTO> committeeDTOS = new ArrayList<>();
        for(CommitteeMember committeeMembers : committeeMemberPage.getContent()){
            User user = userRepository.findById(committeeMembers.getUserId().getId()).get();
                ListPersonBasicDetail listPersonBasicDetail = new ListPersonBasicDetail();

                listPersonBasicDetail.setEmail(user.getEmail());
                listPersonBasicDetail.setFirstName(user.getFirstName());
                listPersonBasicDetail.setGender(user.getGender());
                listPersonBasicDetail.setLastName(user.getLastName());
                listPersonBasicDetail.setMobileno(user.getMobileno());
                listPersonBasicDetail.setPersonId(user.getId());
                listPersonBasicDetail.setProfilePic(user.getProfilePic());
                listPersonBasicDetail.setSurname(user.getSurname());

                listPersonBasicDetail.setHusbandVillageName(user.getHusbandVillageId() != null ? user.getHusbandVillageId() : "");
                listPersonBasicDetail.setHusbandFirstName(user.getHusbandFirstName() != null ? user.getHusbandFirstName() : "");
                listPersonBasicDetail.setHusbandLastName(user.getHusbandLastName() != null ? user.getHusbandLastName() : "");
                listPersonBasicDetail.setHusbandSurname(user.getHusbandSurname() != null ? user.getHusbandSurname() : "");


                listPersonBasicDetail.setCreatedDate(user.getCreatedAt().toString());
//            listPersonBasicDetail.setUpdatedDate(user.getUpdatedAt().toString());
                listPersonBasicDetail.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
//            listPersonBasicDetail.setUpdatedBy(user.getUpdatedBy().getId());
                listPersonBasicDetail.setIsDeleted(user.getIsDeleted());
                listPersonBasicDetail.setStatus(user.getStatus());
                if (user.getVillage() != null) {
                    listPersonBasicDetail.setVillageName(user.getVillage().getName());
                }


            CommitteeDTO committeeDTO = new CommitteeDTO(committeeMembers.getId(),
                    committeeMembers.getCommitteeType().getId(),
                    committeeMembers.getUserId().getId(),
                    listPersonBasicDetail,
                    committeeMembers.getDesignation());
            committeeDTOS.add(committeeDTO);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", committeeDTOS), HttpStatus.OK);
    }

}
