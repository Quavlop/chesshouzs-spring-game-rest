package com.chesshouzs.server.service.http;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.repository.GameActiveRepository;

@Service
public class RestMatchService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

    public GameActiveDto GetMatchData(UUID userId){
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            return null;
        }

        GameActiveDto result = matchData.convertToDto(matchData);
        return result;
    }
}
