package com.xmplify.starter_kit_springboot_singledb.repository;


import com.xmplify.starter_kit_springboot_singledb.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    @Query("SELECT n FROM News n where n.newsType.id = :newsTypeId order by n.createdAt desc")
    Page<News> findByNewsTypeId(String newsTypeId, Pageable pageable);

    @Query("SELECT n FROM News n where n.newsType.id = :newsTypeId and n.adminId.id = :adminId order by n.createdAt desc")
    Page<News> findByNewsTypeId(String newsTypeId, String adminId, Pageable pageable);

    @Query("SELECT n FROM News n where n.adminId.id = :adminId")
    List<News> getByAdminId(String adminId);

    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);
}