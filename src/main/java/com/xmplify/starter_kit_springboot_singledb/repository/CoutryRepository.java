package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoutryRepository  extends JpaRepository<Country, String> {

   boolean existsByName(String name);

   Country findByName(String name);

}
