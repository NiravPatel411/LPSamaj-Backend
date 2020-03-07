package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Education;
import com.xmplify.starter_kit_springboot_singledb.model.State;
import com.xmplify.starter_kit_springboot_singledb.payload.ListStatesResposeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StateRepository  extends JpaRepository<State, String> {

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.ListStatesResposeDTO(s.id,s.name,s.country.id,s.country.name) FROM State s")
    public List<ListStatesResposeDTO> getAllStates();

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.ListStatesResposeDTO(s.id,s.name,s.country.id,s.country.name) FROM State s WHERE s.id = :stateId")
    public ListStatesResposeDTO getStatesById(String stateId);

    @Query("SELECT s FROM State s WHERE s.name = :stateName AND s.country.id = :countryId")
    public State getByNameAndCountry(String countryId, String stateName);

}
