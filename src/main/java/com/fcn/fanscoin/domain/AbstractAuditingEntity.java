package com.fcn.fanscoin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fcn.fanscoin.constant.Constant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity
        implements Serializable {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    private String createdBy = Constant.SYSTEM;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private Instant createdAt = Instant.now();

    @LastModifiedBy
    @Column(name = "updated_by")
    @JsonIgnore
    private String updatedBy = Constant.SYSTEM;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonIgnore
    private Instant updatedAt = Instant.now();
}
