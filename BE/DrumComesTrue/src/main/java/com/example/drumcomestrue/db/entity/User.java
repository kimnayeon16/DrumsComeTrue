package com.example.drumcomestrue.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name= "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userPk;

	@Column(unique = true)
	private String userId;

	@Column
	private String userName;

	@Column
	private String userPw;

	@Column
	private String phoneNumber;

	@Builder
	public User(long userPk, String userId, String userName, String userPw, String phoneNumber) {
		this.userPk = userPk;
		this.userId = userId;
		this.userName = userName;
		this.userPw = userPw;
		this.phoneNumber = phoneNumber;
	}
}
