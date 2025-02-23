package com.zayaanit.aspi.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @Since July 16, 2023
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractModel<U> implements Serializable {

	private static final long serialVersionUID = -7673409459238414720L;

	@CreatedBy
	@Column(name = "zuserid", length = 25)
	private U zuserid;

	@LastModifiedBy
	@Column(name = "zuuserid", length = 25)
	private U zuuserid;

	@CreationTimestamp
	@Column(name = "ztime", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ztime;

	@UpdateTimestamp
	@Column(name = "zutime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date zutime;
}
