package com.chesshouzs.server.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.chesshouzs.server.model.GameSkill;

@Repository
public interface GameSkillRepository extends JpaRepository<GameSkill, UUID>, GameSkillRepositoryImpl {}

interface GameSkillRepositoryImpl {}
