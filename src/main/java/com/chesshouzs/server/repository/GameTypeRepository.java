package com.chesshouzs.server.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chesshouzs.server.model.GameType;

@Repository
public interface GameTypeRepository extends JpaRepository<GameType, UUID>, GameTypeRepositoryImpl {}

interface GameTypeRepositoryImpl {}
