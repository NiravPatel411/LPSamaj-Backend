package com.xmplify.starter_kit_springboot_singledb.mapper;

import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.payload.CommitteeDTO;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommitteeMapper {

    public CommitteeMember convertCommitteeDTOToCommitteeMember(CommitteeDTO committeeDTO) {
        if(Objects.nonNull(committeeDTO.getId())) {
            return new CommitteeMember(committeeDTO.getId(), null, null, committeeDTO.getDesignation());
        } else {
            return new CommitteeMember(null, null, null, committeeDTO.getDesignation());
        }
    }
}
