package com.chesshouzs.server.service.lib.game;

import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.service.lib.usecase.MovementUsecase;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.Helper;
import com.datastax.oss.driver.shaded.guava.common.collect.HashBasedTable;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;


public class King extends Character{
     
    public King(PositionDto position, String color){
        super(color);
        this.position = position;
    }

    public King(String color){
        super(color);
    }

    public Table<Integer, Integer, Boolean> getEligibleMoves(char[][] state){

        Table<Integer, Integer, Boolean> moves = HashBasedTable.create();
        List<BiFunction<Integer, Integer, PositionDto>> eligibleKingMovesResolver = new ArrayList<>();

        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row + 1, col + 1));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row + 1, col - 1));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row - 1, col + 1));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row - 1, col - 1));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row + 1, col));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row - 1, col));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row, col + 1));
        eligibleKingMovesResolver.add((row, col) -> new PositionDto(row, col - 1));

        for (BiFunction<Integer, Integer, PositionDto> validator : eligibleKingMovesResolver){
            PositionDto pos = validator.apply(this.position.getRow(), this.position.getCol());
            moves.put(pos.getRow(), pos.getCol(), true);
        }

        return moves;
    }

    public Boolean isValidMove(PositionDto oldPosition, char[][] oldState, char[][] newState){
        return MovementUsecase.kingMovementValidator(oldPosition, this.position, oldState) && !this.isCurrentKingPositionGuarded(newState);
    }

    public Boolean isAbleToEliminateCheckThreat(Character attacker, PositionDto kingPosition, char[][] oldState){
        // for king only applies to eliminating the attacker
        // this function does not apply enemy coverage checking (implemented in isValidMove.isCurrentKingPositionGuarded)

        return MovementUsecase.kingMovementValidator(attacker.getPosition(), this.position, null);
    }

    public Boolean isCurrentKingPositionGuarded(char[][] state){
        /*
            use utilities from MovementUsecase (except pawn)
            - (queen, rook, bishop) check if the enemy is not actually attacking (blocked by team or enemy themself)
            - (pawn) must use the flipped logic for enemy pawn 
        */

        // queen, rook, bishop
        Boolean longRangeAttackersGuard = MovementUsecase.isPositionFacingDiagonalAttacker(this.position, state, this.color) || MovementUsecase.isPositionFacingFlatDirectionAttacker(this.position, state, this.color);

        Boolean knightAttackersGuard = MovementUsecase.isPositionCoveredByEnemyKnight(this.position, state, this.color);
        Boolean pawnAttackersGuard = MovementUsecase.isPositionCoveredByEnemyPawn(this.position, state, this.color);
        Boolean evolvedPawnAttackersGuard = MovementUsecase.isPositionCoveredByEnemyEvolvedPawn(this.position, state, this.color);
        Boolean enemyKingGuard = MovementUsecase.isPositionCoveredByEnemyKing(this.position, state, this.color);

        return longRangeAttackersGuard || knightAttackersGuard || pawnAttackersGuard || evolvedPawnAttackersGuard || enemyKingGuard;
    }

    /*
        returns {
            is_in_check : [Boolean]
            attackers : [ArrayList<Character.class>]
            invalid_moves : [Table<Integer, Integer, Boolean>] 
            valid_moves : [Table<Integer, Integer, Boolean>]
        }
     */
    public Map<String, Object> inCheckStatus(char[][] state){

        Map<String, Object> data = new HashMap<String, Object>();

        ArrayList<Character> attackers = this.getKingThreateningInstances(state);
        Boolean isInCheck = this.isCurrentKingPositionGuarded(state);
        // Table<Integer, Integer, Boolean> invalidKingMoves = HashBasedTable.create();
        Table<Integer, Integer, Boolean> validKingMoves = HashBasedTable.create();


        Table<Integer, Integer, Boolean> eligibleKingMoves = this.getEligibleMoves(state);
        System.out.println("LMOAOAOAO");
        System.out.println(Helper.convertObjectToJson(eligibleKingMoves.cellSet()));
        for (Table.Cell<Integer, Integer, Boolean> position : eligibleKingMoves.cellSet()){
            // if (MovementUsecase.isPositionGuarded(new PositionDto(position.getRowKey(), position.getColumnKey()), state, this.color)){
            //     invalidKingMoves.put(position.getRowKey(), position.getColumnKey(), true);
            // } else {
            //     validKingMoves.put(position.getRowKey(), position.getColumnKey(), true);
            // }
            if (position.getRowKey() < 0 || position.getColumnKey() < 0 || position.getRowKey() >= state.length || position.getColumnKey() >= state.length){
                continue;
            }

            String squarePieceColor = GameHelper.getPieceColor(state[position.getRowKey()][position.getColumnKey()]);
            if (!MovementUsecase.isPositionGuarded(new PositionDto(position.getRowKey(), position.getColumnKey()), state, this.color) && squarePieceColor != this.color){
                validKingMoves.put(position.getRowKey(), position.getColumnKey(), true);
            }
        }
        System.out.println(Helper.convertObjectToJson(validKingMoves.cellSet()));


        data.put(GameConstants.KEY_ATTACKERS, attackers);
        data.put(GameConstants.KEY_IS_IN_CHECK, isInCheck);
        // data.put(GameConstants.KEY_INVALID_MOVES, invalidKingMoves);
        data.put(GameConstants.KEY_VALID_MOVES, validKingMoves);

        return data;
    }

    public ArrayList<Character> getKingThreateningInstances(char[][] state){
        
        ArrayList<Character> attackers = new ArrayList<Character>();

        // queen, bishop 
        System.out.println(Helper.convertObjectToJson(this.position));
        ArrayList<Character> diagonalAttackers = MovementUsecase.getDiagonalAttackerInstances(this.position, state, this.color);
        ArrayList<Character> flatDirectionAttackers = MovementUsecase.getFlatDirectionAttackerInstances(this.position, state, this.color);
        Character knightAttacker = MovementUsecase.getKnightAttackerInstance(this.position, state, this.color);
        Character pawnAttacker = MovementUsecase.getPawnAttackerInstance(this.position, state, this.color);
        Character evolvedPawnAttacker = MovementUsecase.getEvolvedPawnAttackerInstance(this.position, state, this.color);

        attackers.addAll(diagonalAttackers);
        attackers.addAll(flatDirectionAttackers);
        
        if (knightAttacker != null){
            attackers.add(knightAttacker);
        }

        if (pawnAttacker != null){
            attackers.add(pawnAttacker);
        }

        if (evolvedPawnAttacker != null){
            attackers.add(evolvedPawnAttacker);
        }

        return attackers;
    }

}
