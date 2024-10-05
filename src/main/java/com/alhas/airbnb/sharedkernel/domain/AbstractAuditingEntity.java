package com.alhas.airbnb.sharedkernel.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity<T> implements Serializable {
private static final long serialVersionUID = 1L;
    public abstract T getId();

    @CreatedDate
    @Column(updatable = false,name = "created_date")
    private Instant createdDate=Instant.now();

    private Instant lastModifiedDate=Instant.now();


}
