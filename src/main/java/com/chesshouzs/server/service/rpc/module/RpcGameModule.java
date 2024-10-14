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
import com.chesshouzs.server.util.Helper;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;

@Configuration
public class RpcGameModule {

    
    public Boolean validateMovement(String oldStateNotation, String newStateNotation){

        oldStateNotation = Helper.preprocessStateNotation(oldStateNotation);
        newStateNotation = Helper.preprocessStateNotation(newStateNotation);

        System.out.println("INSIDE METHOD : OLD STATE : ");
        System.out.println(oldStateNotation);
        System.out.println(oldStateNotation.length());
        for (byte b : oldStateNotation.getBytes()) {
            System.out.print(b + " ");
        }
        System.out.println("INSIDE METHOD : NEW STATE : ");
        System.out.println(newStateNotation);
        System.out.println(newStateNotation.length());  
        for (byte b : newStateNotation.getBytes()) {
            System.out.print(b + " ");
        }
        System.out.println("\n");

        oldStateNotation = Helper.formatGameNotation(oldStateNotation);
        newStateNotation = Helper.formatGameNotation(newStateNotation);

        char[][] oldState = GameHelper.convertNotationToArray(oldStateNotation);
        char[][] newState = GameHelper.convertNotationToArray(newStateNotation);
        System.out.println(oldState.length); 
        System.out.println(newState.length);

        // validate if there is no duplicate movement
        // get character metadata (character object)
        System.out.println("CHECKPOINT - 0");
        Map<String, Object> movement = Game.getMovementData(oldState, newState);
        if (!(Boolean)movement.get(GameConstants.KEY_VALID_MOVE)){
            return false;
        }
        System.out.println("CHECKPOINT - 1");

        // this uses new position from newState
        Character character = (Character)movement.get(GameConstants.KEY_CHARACTER);

        if (!character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldState, newState)){
            return false;
        }
        System.out.println("CHECKPOINT - 2");


        // if new king position is in check, then of course movement is invalid 
        // this covers the needs of pinned piece validation
        King newKingPosition = CharacterUsecase.findKing(oldState, character.getColor());        
        if (newKingPosition == null){
            return false;
        }
        System.out.println("CHECKPOINT - 3");

        Boolean newInCheckStatus = newKingPosition.isCurrentKingPositionGuarded(newState);
        System.out.println("LEELLLLL " + newInCheckStatus);
        if (newInCheckStatus){
            return false;
        }
        System.out.println("CHECKPOINT - 4");

        
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
                System.out.println("CHECKPOINT - 5");


                if (attackers.size() > 1){
                    // discovered check
                    // this will directly trigger a checkmate
                    // TODO : trigger checkmate
                    // return true
                }
            } 

            if (attackers.size() > 1){
                // discovered check
                System.out.println("CHECKPOINT - 6");
                if (!(character instanceof King)){
                    // directly return false because king has to move if still movable
                    // (other teammate movement will not save the king)
                    return false;
                }
                System.out.println("CHECKPOINT - 7");

            } else if (!(character instanceof King)) {
                // if king still have valid moves but teammate decides to block the attack / eliminate the attacker

                // the position below references new position from newState
                // PositionDto newMovingCharacterPosition = character.getPosition();            

                System.out.println("CHECKPOINT - 8");
                if (attackers.size() <= 0){
                    return true;
                }   
                System.out.println("CHECKPOINT - 9");


                Character attackerObj = null;
                for (Character attacker : attackers){
                    attackerObj = attacker;
                } 

                // if moving character new movement is not eliminating the threat then it is invalid
                // if (attackerObj != null && !character.isAbleToEliminateCheckThreat(attackerObj, king.getPosition(), oldState)){
                //     return false;
                // }
            } 
            // rest if king still have valid moves and decides to eliminate the attacker (blocking is not possible)
            // implemented in check of king is new position guarded above
        }

        // TODO : stalemate
        System.out.println("CHECKPOINT - 10");

        return true;
    }
}
