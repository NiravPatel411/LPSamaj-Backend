package com.xmplify.starter_kit_springboot_singledb.repository;

import com.xmplify.starter_kit_springboot_singledb.model.NewsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsTypeRepository extends JpaRepository<NewsType,String> {

    //    List<NewsType> findAllByIdOrderByPriority_numberDesc();
    @Query("SELECT n FROM NewsType n order by n.priority_number desc")
    public List<NewsType> findAllByOrOrderByPriority_number();

}
