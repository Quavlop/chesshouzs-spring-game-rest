package com.chesshouzs.server.repository.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.chesshouzs.server.model.cassandra.keys.PlayerGameStatePrimaryKeys;
import com.chesshouzs.server.model.cassandra.tables.PlayerGameState;

public interface PlayerGameStatesRepository extends CassandraRepository<PlayerGameState, PlayerGameStatePrimaryKeys>{}
