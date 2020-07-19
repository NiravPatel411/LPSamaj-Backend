package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "select year(date) as 'year' from account group by year(date) order by date desc ", nativeQuery = true)
    List<String> getListOfYear();

    @Query(value = "SELECT * FROM `account` WHERE year(date)=:selectedYear order by date desc", nativeQuery = true)
    List<Account> getAccountByYear(String selectedYear);

    @Query(value = "SELECT * FROM `account` WHERE year(date)<:selectedYear", nativeQuery = true)
    List<Account> getAccountByBeforeYear(String selectedYear);

}
