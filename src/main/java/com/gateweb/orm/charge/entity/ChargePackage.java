/*
 * This file is generated by jOOQ.
 */
package com.gateweb.orm.charge.entity;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Entity
@Table(name = "charge_package", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "charge_package_pkey", columnNames = {"package_id"})
})
public class ChargePackage implements Serializable {

    private static final long serialVersionUID = -75072950;

    private Long packageId;
    private Long parentId;
    private String name;
    private Boolean enabled;
    private Long creatorId;
    private LocalDateTime createDate;
    private Long modifierId;
    private LocalDateTime modifyDate;

    public ChargePackage() {
    }

    public ChargePackage(Long packageId, Long parentId, String name, boolean enabled, Long creatorId, LocalDateTime createDate, Long modifierId, LocalDateTime modifyDate) {
        this.packageId = packageId;
        this.parentId = parentId;
        this.name = name;
        this.enabled = enabled;
        this.creatorId = creatorId;
        this.createDate = createDate;
        this.modifierId = modifierId;
        this.modifyDate = modifyDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false, precision = 64)
    public Long getPackageId() {
        return this.packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name = "parent_id", precision = 64)
    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Column(name = "name", length = 30)
    @Size(max = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "creator_id", precision = 64)
    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @CreationTimestamp
    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Column(name = "modifier_id", precision = 64)
    public Long getModifierId() {
        return this.modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    @UpdateTimestamp
    @Column(name = "modify_date")
    public LocalDateTime getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "ChargePackage{" +
                "packageId=" + packageId +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", creatorId=" + creatorId +
                ", createDate=" + createDate +
                ", modifierId=" + modifierId +
                ", modifyDate=" + modifyDate +
                '}';
    }
}