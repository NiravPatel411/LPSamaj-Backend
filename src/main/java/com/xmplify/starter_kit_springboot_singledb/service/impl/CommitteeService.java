package com.xmplify.starter_kit_springboot_singledb.service.impl;

import com.xmplify.starter_kit_springboot_singledb.mapper.CommitteeMapper;
import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeMemberRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.CommitteeTypeRepository;
import com.xmplify.starter_kit_springboot_singledb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommitteeService {
    @Autowired
    CommitteeMapper committeeMapper;

    @Autowired
    CommitteeTypeRepository committeeTypeRepository;

    @Autowired
    CommitteeMemberRepository committeeMemberRepository;

    @Autowired
    UserRepository userRepository;

    public CommitteeMember AddUpdateCommittee(CommitteeDTO committeeDTO) {
        CommitteeMember committeeMember = committeeMapper.convertCommitteeDTOToCommitteeMember(committeeDTO);
        committeeMember.setCommitteeType(committeeTypeRepository.findById(committeeDTO.getCommitteeTypeId()).get());
        committeeMember.setUserId(userRepository.findById(committeeDTO.getPersonId()).get());
        return committeeMemberRepository.save(committeeMember);
    }
}
