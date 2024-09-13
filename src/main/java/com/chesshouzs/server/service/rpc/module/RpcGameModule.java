package com.chesshouzs.server.service.rpc.module;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.util.GameHelper;

public class RpcGameModule {

    private RedisBaseRepository redis; 

    RpcGameModule(RedisBaseRepository redis){
        this.redis = redis;
    }
    
    public Boolean validateMovement(String newStateNotation){

        char[][] newState = GameHelper.convertNotationToArray(newStateNotation);

        // Map<String, Object> movement = Game.getMovementData(null, null);
        //         String key = RedisConstants.getGameMoveKey(matchData.getMovesCacheRef());
        // Map<String, String> notation = redis.hgetall(key);
        // if (notation == null){
        //     throw new Exception("Match state not found");
        // }


        return true;
    }
}
