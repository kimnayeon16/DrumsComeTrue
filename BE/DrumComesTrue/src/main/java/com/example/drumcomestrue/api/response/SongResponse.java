package com.example.drumcomestrue.api.response;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongResponse {

	private long musicId;
	private String musicName;
	private String musicMaker;
	private String musicImg;
	private String musicSheet;
	private String firstBPM;
	private String SecondBPM;

	@Builder
	public SongResponse(long musicId, String musicName, String musicMaker, String musicImg, String musicSheet,
		String firstBPM, String secondBPM) {
		this.musicId = musicId;
		this.musicName = musicName;
		this.musicMaker = musicMaker;
		this.musicImg = musicImg;
		this.musicSheet = musicSheet;
		this.firstBPM = firstBPM;
		SecondBPM = secondBPM;
	}
}
