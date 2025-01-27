package com.chesshouzs.server.service.lib.game;

import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.service.lib.usecase.MovementUsecase;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;
import com.chesshouzs.server.dto.custom.match.PositionDto;


public class Bishop extends Character{

    public Bishop(String color){
        super(color);
    }    

    public Bishop(String color, PositionDto position){
        super(color, position);
    }        

    public Table<Integer, Integer, Boolean> getEligibleMoves(char[][] state){
        return null;
    }

    public Boolean isValidMove(PositionDto oldPosition, char[][] oldState, char[][] newState){
        return MovementUsecase.diagonalMovementValidator(oldPosition, this.position, oldState);
    }

    public Boolean isAbleToEliminateCheckThreat(Character attacker, PositionDto kingPosition, char[][] oldState){
        // these three pieces attack can't be block
        // therefore eliminator must directly eliminate the attacker

        Boolean eliminatesAttacker = this.position.getRow() == attacker.getPosition().getRow() && this.position.getCol() == attacker.getPosition().getCol();

        if (attacker instanceof Pawn || attacker instanceof EvolvedPawn || attacker instanceof Knight){
            return eliminatesAttacker;
        }

        // check if in between the attacker and the king AND check if king and attacker is face to face 

        if (attacker instanceof Rook){
            if (MovementUsecase.isTwoPositionFaceToFaceFlat(oldState, kingPosition, attacker.getPosition(), this.color) && MovementUsecase.isPositionBetweenTwoPositionFlatly(this.position, kingPosition, attacker.getPosition())){
                return true;   
            }
        }

        if (attacker instanceof Bishop){
            if (MovementUsecase.isTwoPositionFaceToFaceDiagonal(oldState, kingPosition, attacker.getPosition(), this.color) && MovementUsecase.isPositionBetweenTwoPositionDiagonally(this.position, kingPosition, attacker.getPosition())){
                return true;   
            }
        }

        if (attacker instanceof Queen){
            if (
                (MovementUsecase.isTwoPositionFaceToFaceFlat(oldState, kingPosition, attacker.getPosition(), this.color) && MovementUsecase.isPositionBetweenTwoPositionFlatly(this.position, kingPosition, attacker.getPosition())) 
                || 
                (MovementUsecase.isTwoPositionFaceToFaceDiagonal(oldState, kingPosition, attacker.getPosition(), this.color) && MovementUsecase.isPositionBetweenTwoPositionDiagonally(this.position, kingPosition, attacker.getPosition()))
            ){
                return true;   
            }
        }

        return eliminatesAttacker;       
    }

}
