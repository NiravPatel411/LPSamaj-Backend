package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Country;
import com.xmplify.starter_kit_springboot_singledb.model.Education;
import com.xmplify.starter_kit_springboot_singledb.payload.StatesByCountryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CoutryRepository  extends JpaRepository<Country, String> {

   boolean existsByName(String name);

   Country findByName(String name);

}
