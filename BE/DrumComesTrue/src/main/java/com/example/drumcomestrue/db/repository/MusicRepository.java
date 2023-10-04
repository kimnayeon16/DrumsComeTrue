package com.example.drumcomestrue.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.drumcomestrue.db.entity.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
	Music findByMusicName(String musicName);
}
