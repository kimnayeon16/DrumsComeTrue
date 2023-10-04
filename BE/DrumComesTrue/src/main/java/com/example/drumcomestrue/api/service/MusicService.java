package com.example.drumcomestrue.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.drumcomestrue.api.response.MusicResponse;
import com.example.drumcomestrue.api.response.SongResponse;
import com.example.drumcomestrue.db.entity.Music;
import com.example.drumcomestrue.db.repository.MusicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {

	private final MusicRepository musicRepository;
	public List<MusicResponse> allshow() {

		List<MusicResponse> musicResponses = new ArrayList<>();

		List<Music> musicList = musicRepository.findAll();

		for (Music music: musicList) {
			MusicResponse musicResponse = new MusicResponse();
			musicResponse.setMusicName(music.getMusicName());
			musicResponse.setMusicMaker(music.getMusicMaker());
			musicResponse.setMusicImage(music.getMusicImg());
			musicResponses.add(musicResponse);
		}
		return musicResponses;
	}

	public SongResponse oneshow(String musicName) {
		Music music = musicRepository.findByMusicName(musicName);
		SongResponse songResponse = SongResponse.builder()
			.musicName(music.getMusicName())
			.firstBPM(music.getFirstBPM())
			.musicImg(music.getMusicImg())
			.musicSheet(music.getMusicSheet())
			.secondBPM(music.getSecondBPM())
			.musicMaker(music.getMusicMaker())
			.build();
		return songResponse;
	}
}
