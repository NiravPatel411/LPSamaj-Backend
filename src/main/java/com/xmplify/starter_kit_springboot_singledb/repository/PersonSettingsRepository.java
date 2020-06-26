package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.PersonSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonSettingsRepository extends JpaRepository<PersonSetting, String> {

    List<PersonSetting> findAllByPersonId(String personId);

    @Query("SELECT s FROM PersonSetting s WHERE s.person.id = :personId")
    PersonSetting findByPersonId(String personId);
}
