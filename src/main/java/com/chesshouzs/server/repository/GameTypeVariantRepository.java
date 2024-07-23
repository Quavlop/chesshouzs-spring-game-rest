package com.chesshouzs.server.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chesshouzs.server.model.GameTypeVariant;

@Repository
public interface GameTypeVariantRepository extends JpaRepository<GameTypeVariant, UUID>, GameTypeRepositoryImpl {}


interface GameTypeVariantRepositoryImpl {}
