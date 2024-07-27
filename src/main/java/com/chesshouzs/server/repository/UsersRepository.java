package com.chesshouzs.server.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chesshouzs.server.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID>, UsersRepositoryImpl {}


interface UsersRepositoryImpl {}