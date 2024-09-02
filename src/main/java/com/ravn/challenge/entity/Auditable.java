package com.ravn.challenge.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class Auditable {

	@CreatedBy
	@Column(length = 55, updatable = false)
	protected String createdBy;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	protected LocalDateTime createdDate;
	
	@LastModifiedBy
	@Column(length = 55)
	protected String lastModifiedBy;
	
	@LastModifiedDate
	protected LocalDateTime lastModifiedDate;
}
