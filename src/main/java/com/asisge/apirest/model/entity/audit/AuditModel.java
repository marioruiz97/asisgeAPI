package com.asisge.apirest.model.entity.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public @Data class AuditModel<U> {

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private U createdBy;

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@LastModifiedBy
	@Column(name = "last_modified_by")
	private U lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

	@PrePersist
	public void setCreatedDate(Date createdDate) {
		this.createdDate = new Date();
	}

}
