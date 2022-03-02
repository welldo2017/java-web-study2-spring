package com.welldo.spring.spring10.bean;

import javax.persistence.PrePersist;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public abstract class AbstractEntity {

	private Long id;
	private long createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}


	public ZonedDateTime getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
	}


	/**
	 * 这个方法没用.
	 * 因为这个是jpa的接口,需要一个实现才行. mybatis不是jpa的实现.
	 * {@link com.welldo.spring.spring8.bean.AbstractEntity}
	 */
	@PrePersist
	public void preInsert() {
		setCreatedAt(System.currentTimeMillis());
	}




}
