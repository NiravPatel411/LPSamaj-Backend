package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class AditableEntity implements Serializable{

     @CreationTimestamp
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     @Column(nullable = false, updatable = false)
     private Date createdAt;

     @LastModifiedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private Date updatedAt;

     @ManyToOne
     private Admin createdBy;

     @ManyToOne
     private Admin updatedBy;

    @ManyToOne
    private Admin deletedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    private int isDeleted = 0;

    private String status = "Active";

}
