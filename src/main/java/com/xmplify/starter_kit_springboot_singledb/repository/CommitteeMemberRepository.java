package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember, String> {

    @Query("SELECT c FROM CommitteeMember c WHERE c.committeeType.id = :typeId")
    Page<CommitteeMember> getCommitteeMemberByCommitteeTypeId(String typeId, Pageable pageable);

    boolean existsByCommitteeTypeIdAndUserIdId(String committeeType, String userId);

    void deleteByCommitteeTypeId(String CommitteeType);
}
