package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.District;
import com.xmplify.starter_kit_springboot_singledb.model.Village;
import com.xmplify.starter_kit_springboot_singledb.payload.VillageListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VillageRepository extends JpaRepository<Village, String> {

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.VillageListDTO(" +
            "v.id," +
            "v.name," +
            "v.district.id," +
            "v.district.name," +
            "v.district.state.id," +
            "v.district.state.name," +
            "v.district.state.country.id," +
            "v.district.state.country.name) FROM Village v")
    public List<VillageListDTO> getAllVillage();

    @Query("SELECT new com.xmplify.starter_kit_springboot_singledb.payload.VillageListDTO(" +
            "v.id," +
            "v.name," +
            "v.district.id," +
            "v.district.name," +
            "v.district.state.id," +
            "v.district.state.name," +
            "v.district.state.country.id," +
            "v.district.state.country.name) FROM Village v WHERE v.id = :villageId")
    public VillageListDTO getVillageById(String villageId);

    public Optional<Village> findById(String villageId);

    @Query("SELECT v FROM Village v WHERE v.name = :villageName AND v.district.id = :districtId")
    public Optional<Village> getVillageByNameAndDistrict_Id(String villageName,String districtId);

}
