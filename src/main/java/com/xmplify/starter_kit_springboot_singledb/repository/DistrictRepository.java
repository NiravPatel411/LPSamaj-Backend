package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.payload.DistrictListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, String> {

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.DistrictListDTO(dist.id,dist.name,dist.state.id,dist.state.name,dist.state.country.id,dist.state.country.name) FROM District dist")
    List<DistrictListDTO> getDistrictList();

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.DistrictListDTO(dist.id,dist.name,dist.state.id,dist.state.name,dist.state.country.id,dist.state.country.name) FROM District dist WHERE dist.id = :distId")
    DistrictListDTO getDistrictById(String distId);

    @Query("SELECT d FROM District d WHERE d.name = :districtName AND d.state.id = :stateId")
    Optional<District> getByDistrictNameAndStateId(String districtName, String stateId);
}
