package com.xmplify.starter_kit_springboot_singledb.controller;

import com.xmplify.starter_kit_springboot_singledb.DTOs.PersonBasicDetailDTO1;
import com.xmplify.starter_kit_springboot_singledb.constants.GlobalConstants;
import com.xmplify.starter_kit_springboot_singledb.mapper.CommitteeMapper;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeType;
import com.xmplify.starter_kit_springboot_singledb.model.User;
import com.xmplify.starter_kit_springboot_singledb.payload.ApiResponse;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeMemberRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeTypeRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import com.xmplify.starter_kit_springboot_singledb.service.CommitteeService;
import com.xmplify.starter_kit_springboot_singledb.service.UserService;
import com.xmplify.starter_kit_springboot_singledb.service.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/committee")
public class CommitteeController {

    final
    UserRepository userRepository;
    private final CommitteeMemberRepository committeeMemberRepository;
    private final
    CommitteeService committeeService;
    private final
    CommitteeMapper committeeMapper;
    private final UserService userService;
    private final
    Validators validators;
    private final
    CommitteeTypeRepository committeeTypeRepository;

    @Autowired
    public CommitteeController(CommitteeMemberRepository committeeMemberRepository, CommitteeService committeeService, CommitteeMapper committeeMapper, UserService userService, Validators validators, CommitteeTypeRepository committeeTypeRepository, UserRepository userRepository) {
        this.committeeMemberRepository = committeeMemberRepository;
        this.committeeService = committeeService;
        this.committeeMapper = committeeMapper;
        this.userService = userService;
        this.validators = validators;
        this.committeeTypeRepository = committeeTypeRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/addUpdateCommitteeMember")
    public ResponseEntity<?> addUpdateCommittee(@RequestBody CommitteeDTO committeeDTO) {
        if (Objects.isNull(committeeDTO)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "RequestBody not found"), HttpStatus.BAD_REQUEST);
        }
        if (Objects.nonNull(committeeDTO.getId())) {
            List<String> messages = validators.validateUpdateCommitteeDTO(committeeDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Updated", committeeService.AddUpdateCommittee(committeeDTO)), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddCommitteeDTO(committeeDTO);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Added", committeeService.AddUpdateCommittee(committeeDTO)), HttpStatus.OK);

        }
    }

    @DeleteMapping("/removeCommitteeMember/{committeeId}")
    public ResponseEntity<?> deleteCommittee(@PathVariable String committeeId) {
        if (committeeMemberRepository.existsById(committeeId)) {
            committeeMemberRepository.delete(committeeMemberRepository.findById(committeeId).get());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Deleted", "Successfully Deleted"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "Committee not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/removeCommitteeType/{committeeTypeId}")
    @Transactional
    public ResponseEntity<?> deleteCommitteeTypeId(@PathVariable String committeeTypeId) {
        if (committeeTypeRepository.existsById(committeeTypeId)) {
            committeeMemberRepository.deleteByCommitteeTypeId(committeeTypeId);
            committeeTypeRepository.delete(committeeTypeRepository.findById(committeeTypeId).get());
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Deleted", "Successfully Deleted"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "CommitteeType not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addCommitteeType")
    public ResponseEntity<?> addCommitteeType(@RequestBody CommitteeType committeeType) {
        if (Objects.isNull(committeeType)) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", "RequestBody not found"), HttpStatus.BAD_REQUEST);
        }

        if (Objects.nonNull(committeeType.getId())) {
            List<String> messages = validators.validateUpdateCommitteeType(committeeType);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        } else {
            List<String> messages = validators.validateAddCommitteeType(committeeType);
            if (!messages.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST.value(), false, "BAD_REQUEST", messages), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Added", committeeTypeRepository.save(committeeType)), HttpStatus.OK);
        }
    }

    @GetMapping("/committeeType")
    public ResponseEntity<?> getAllCommitteeType() {
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", committeeTypeRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/committeeByType/{type}")
    public ResponseEntity<?> getCommitteeByType(@PathVariable String type, @PageableDefault(page = 0, size = GlobalConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        Page<CommitteeMember> committeeMemberPage = committeeMemberRepository.getCommitteeMemberByCommitteeTypeId(type, pageable);
        List<CommitteeDTO> committeeDTOS = new ArrayList<>();
        for (CommitteeMember committeeMembers : committeeMemberPage.getContent()) {
            User user = userRepository.findById(committeeMembers.getUserId().getId()).get();
            PersonBasicDetailDTO1 personBasicDetailDTO = new PersonBasicDetailDTO1();

            personBasicDetailDTO.setEmail(user.getEmail());
            personBasicDetailDTO.setFirstName(user.getFirstName());
            personBasicDetailDTO.setGender(user.getGender());
            personBasicDetailDTO.setLastName(user.getLastName());
            personBasicDetailDTO.setMobileno(user.getMobileno());
            personBasicDetailDTO.setPersonId(user.getId());
            personBasicDetailDTO.setProfilePic(user.getProfilePic());
            personBasicDetailDTO.setSurname(user.getSurname());

            personBasicDetailDTO.setHusbandVillageName(user.getHusbandVillageId() != null ? user.getHusbandVillageId() : "");
            personBasicDetailDTO.setHusbandFirstName(user.getHusbandFirstName() != null ? user.getHusbandFirstName() : "");
            personBasicDetailDTO.setHusbandLastName(user.getHusbandLastName() != null ? user.getHusbandLastName() : "");
            personBasicDetailDTO.setHusbandSurname(user.getHusbandSurname() != null ? user.getHusbandSurname() : "");


            personBasicDetailDTO.setCreatedDate(user.getCreatedAt().toString());
//            personBasicDetailDTO.setUpdatedDate(user.getUpdatedAt().toString());
            personBasicDetailDTO.setCreatedBy(user.getCreatedBy() != null ? user.getCreatedBy().getId() : null);
//            personBasicDetailDTO.setUpdatedBy(user.getUpdatedBy().getId());
            personBasicDetailDTO.setIsDeleted(user.getIsDeleted());
            personBasicDetailDTO.setStatus(user.getStatus());
            if (user.getVillage() != null) {
                personBasicDetailDTO.setVillageName(user.getVillage().getName());
            }


            CommitteeDTO committeeDTO = new CommitteeDTO(committeeMembers.getId(),
                    committeeMembers.getCommitteeType().getId(),
                    committeeMembers.getUserId().getId(),
                    personBasicDetailDTO,
                    committeeMembers.getDesignation());
            committeeDTOS.add(committeeDTO);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK.value(), true, "Success", committeeDTOS), HttpStatus.OK);
    }

}
