package com.chesshouzs.server.service.http;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import com.chesshouzs.server.dto.GameTypesDto;
import com.chesshouzs.server.repository.GameTypeRepository;
import com.chesshouzs.server.model.GameType;


interface GameServiceInterface {
    List<GameTypesDto>  GetGameTypes();
}


@Service
public class RestGameService implements GameServiceInterface{

    @Autowired
    public GameTypeRepository gameTypeRepository;
    
    public List<GameTypesDto> GetGameTypes(){

        List<GameType> gameTypes = gameTypeRepository.findAll();
        if (gameTypes.isEmpty()){
            return null;
        }
        
        List<GameTypesDto> gameTypesDto = new ArrayList<GameTypesDto>();
        for (GameType gameType : gameTypes){
            gameTypesDto.add(gameType.convertToDto(gameType));
        }
        
        return gameTypesDto;

    }

}
