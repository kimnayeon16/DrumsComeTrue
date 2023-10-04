package com.example.drumcomestrue.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Music {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long musicId;

	@Column(unique = true)
	private String musicName;

	@Column
	private String musicMaker;

	@Column
	private String musicImg;

	@Column
	private String musicSheet;

	@Column
	private String firstBPM;

	@Column
	private String SecondBPM;

}
