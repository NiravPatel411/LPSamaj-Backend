package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AditableEntity implements Serializable{

     @CreatedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private Timestamp createdDate;

     @LastModifiedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private Timestamp lastModifiedDate;

     @ManyToOne
     @CreatedBy
     private Admin createdBy;

     @ManyToOne
     @LastModifiedBy
     private Admin lastModifiedBy;

     private int isDeleted = 0;

    private String status = "Active";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Admin createdBy) {
        this.createdBy = createdBy;
    }

    public Admin getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Admin lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
