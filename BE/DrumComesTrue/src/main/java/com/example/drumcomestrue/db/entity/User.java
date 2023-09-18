// package com.example.drumcomestrue.db.entity;
//
// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
// import javax.persistence.Table;
//
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.NoArgsConstructor;
//
// @Entity
// @NoArgsConstructor
// @Table(name= "user")
// public class User {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	private long userId;
//
// 	@Column
// 	private String userName;
//
// 	@Column
// 	private String userPw;
//
// 	@Column
// 	private String userAge;
//
// 	@Builder
// 	public User(long userId, String userName, String userAge) {
// 		this.userId = userId;
// 		this.userName = userName;
// 		this.userAge = userAge;
// 	}
// }
