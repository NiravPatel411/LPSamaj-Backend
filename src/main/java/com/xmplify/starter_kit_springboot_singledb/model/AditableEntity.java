package com.xmplify.starter_kit_springboot_singledb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AditableEntity implements Serializable{

     @CreatedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private Timestamp createdAt;

     @LastModifiedDate
     @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
     private Timestamp updatedAt;

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
    private Timestamp deletedAt;
    private int isDeleted = 0;

    public Admin getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Admin deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Admin createdBy) {
        this.createdBy = createdBy;
    }

    public Admin getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Admin updatedBy) {
        this.updatedBy = updatedBy;
    }
}
