package com.daou.batch.model;

import java.time.LocalDate;

import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseEntity {
	private LocalDate createdDate = LocalDate.now();
	private LocalDate lastModifiedDate = LocalDate.now();

	public void create(LocalDate date) {
		this.createdDate = date;
	}

	void update() {
		this.lastModifiedDate = LocalDate.now();
	}

}
