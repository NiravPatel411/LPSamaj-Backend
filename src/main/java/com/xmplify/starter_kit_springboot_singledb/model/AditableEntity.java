package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class AditableEntity implements Serializable{

     @CreatedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private LocalDateTime createdAt;

     @LastModifiedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private LocalDateTime updatedAt;

     @ManyToOne
     @CreatedBy
     private Admin createdBy;

     @ManyToOne
     @LastModifiedBy
     private Admin updatedBy;

    @ManyToOne
    @LastModifiedBy
    private Admin deletedBy;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedAt;

    private int isDeleted = 0;

    private String status = "Active";

}
