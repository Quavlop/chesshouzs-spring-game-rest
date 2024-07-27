package com.chesshouzs.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chesshouzs.server.model.GameActive;

public interface GameActiveRepository extends JpaRepository<GameActive, UUID>, GameActiveRepositoryImpl {
    @Query(nativeQuery = true, value = "SELECT * FROM game_active WHERE white_player_id = ?1 OR black_player_id = ?1 AND is_done = false LIMIT 1")
    GameActive findPlayerActiveMatch(UUID userId);
}

interface GameActiveRepositoryImpl{}
