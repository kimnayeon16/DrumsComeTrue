package com.example.drumcomestrue.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drumcomestrue.api.response.MusicResponse;
import com.example.drumcomestrue.api.response.SongResponse;
import com.example.drumcomestrue.api.service.MusicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/music")
@RequiredArgsConstructor
public class MusicController {

	private final MusicService musicService;

	@GetMapping("/allshow")
	public List<MusicResponse> showAll(){
		List<MusicResponse> musicResponses = musicService.allshow();
		return musicResponses;
	}

	@GetMapping("/oneshow/{musicName}")
	public SongResponse oneshow(@PathVariable("musicName") String musicName){
		SongResponse songResponse = musicService.oneshow(musicName);
		return songResponse;
	}

}
