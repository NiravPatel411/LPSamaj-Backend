package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.CommitteeMember;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.VillageListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember,String> {

    @Query("SELECT c FROM CommitteeMember c WHERE c.committeeType.id = :typeId")
    public Page<CommitteeMember> getCommitteeMemberByCommitteeTypeId(String typeId, Pageable pageable);

    public boolean existsByCommitteeTypeIdAndUserIdId(String committeeType,String userId);

    public void deleteByCommitteeTypeId(String CommitteeType);
}
