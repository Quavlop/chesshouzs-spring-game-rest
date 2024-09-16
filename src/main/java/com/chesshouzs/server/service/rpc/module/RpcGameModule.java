package com.chesshouzs.server.service.rpc.module;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.game.King;
import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.service.lib.usecase.CharacterUsecase;
import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.util.GameHelper;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;

@Configuration
public class RpcGameModule {

    
    public Boolean validateMovement(String oldStateNotation, String newStateNotation){

        char[][] oldState = GameHelper.convertNotationToArray(oldStateNotation);
        char[][] newState = GameHelper.convertNotationToArray(newStateNotation);

        // validate if there is no duplicate movement
        // get character metadata (character object)
        Map<String, Object> movement = Game.getMovementData(oldState, newState);
        if (!(Boolean)movement.get(GameConstants.KEY_VALID_MOVE)){
            return false;
        }

        // this uses new position from newState
        Character character = (Character)movement.get(GameConstants.KEY_CHARACTER);

        if (!character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldState, newState)){
            return false;
        }

        // if new king position is in check, then of course movement is invalid 
        // this covers the needs of pinned piece validation
        King newKingPosition = CharacterUsecase.findKing(oldState, character.getColor());        
        Map<String, Object> newInCheckStatus = newKingPosition.inCheckStatus(newState);
        if ((Boolean)newInCheckStatus.get(GameConstants.KEY_IS_IN_CHECK)){
            return false;
        }
        
        King king = CharacterUsecase.findKing(oldState, character.getColor());
        Map<String, Object> inCheckStatus = king.inCheckStatus(oldState);

        if ((Boolean)inCheckStatus.get(GameConstants.KEY_IS_IN_CHECK)){ 
            // if king is in check 

            ArrayList<Character> attackers = (ArrayList<Character>)inCheckStatus.get(GameConstants.KEY_ATTACKERS);

            Table<Integer, Integer, Boolean> eligibleKingMoves = (Table<Integer, Integer, Boolean>)inCheckStatus.get(GameConstants.KEY_VALID_MOVES);
        
            // if king has no valid moves
            if (eligibleKingMoves.isEmpty()){
                // directly return false if king is forcing to move
                if (character instanceof King){
                    return false;
                }

                if (attackers.size() > 1){
                    // discovered check
                    // this will directly trigger a checkmate
                    // TODO : trigger checkmate
                    // return false
                }
            } 

            if (attackers.size() > 1){
                // discovered check
                if (!(character instanceof King)){
                    // directly return false because king has to move if still movable
                    // (other teammate movement will not save the king)
                    return false;
                }
            } else if (!(character instanceof King)) {
                // if king still have valid moves but teammate decides to block the attack / eliminate the attacker

                // the position below references new position from newState
                // PositionDto newMovingCharacterPosition = character.getPosition();            

                if (attackers.size() <= 0){
                    return false;
                }   

                Character attackerObj = null;
                for (Character attacker : attackers){
                    attackerObj = attacker;
                } 

                // if moving character new movement is not eliminating the threat then it is invalid
                if (attackerObj != null && !character.isAbleToEliminateCheckThreat(attackerObj, king.getPosition(), oldState)){
                    return false;
                }
            } else {
                // if king still have valid moves and decides to eliminate the attacker (blocking is not possible)
            }
        }

        // TODO : stalemate

        return true;
    }
}
