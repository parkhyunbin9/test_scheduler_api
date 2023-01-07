package com.daou.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(name = "user_role", length = 20)
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Builder
	public User(String username, String password, UserRole role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
