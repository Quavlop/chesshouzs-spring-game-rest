package com.chesshouzs.server.service.lib.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.ArrayList;

import org.hibernate.query.results.PositionalSelectionsNotAllowedException;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.game.Bishop;
import com.chesshouzs.server.service.lib.game.EvolvedPawn;
import com.chesshouzs.server.service.lib.game.King;
import com.chesshouzs.server.service.lib.game.Knight;
import com.chesshouzs.server.service.lib.game.Pawn;
import com.chesshouzs.server.service.lib.game.Queen;
import com.chesshouzs.server.service.lib.game.Rook;
import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.util.GameHelper;

import io.lettuce.core.StringMatchResult.Position;

public class MovementUsecase {
    public static Boolean straightMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        return oldPosition.getRow() == newPosition.getRow() ^ oldPosition.getCol() == newPosition.getCol();
    }

    public static Boolean diagonalMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        int rowDiff = Math.abs(oldPosition.getRow() - newPosition.getRow());
        int colDiff = Math.abs(oldPosition.getCol() - newPosition.getCol());
        return rowDiff == colDiff && rowDiff > 0 && colDiff > 0;
    }

    public static Boolean kingMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        int rowDiff = Math.abs(oldPosition.getRow() - newPosition.getRow());
        int colDiff = Math.abs(oldPosition.getCol() - newPosition.getCol());
        return (diagonalMovementValidator(oldPosition, newPosition, oldState) || straightMovementValidator(oldPosition, newPosition, oldState))
            && (rowDiff == 1 || colDiff == 1);
    }

    public static Boolean knightShapeMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        int rowDiff = Math.abs(oldPosition.getRow() - newPosition.getRow());
        int colDiff = Math.abs(oldPosition.getCol() - newPosition.getCol());
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    public static Boolean pawnForwardMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        int boardSize = oldState.length; 
        int step = 1;

        if (oldPosition.getRow() == boardSize - 3){
            step = 2;
        }

        int diff = oldPosition.getRow() - newPosition.getRow();

        if (diff == step && step == 2 && !GameHelper.isEmptySquare(oldState[oldPosition.getRow()-1][oldPosition.getCol()])){
            return false;
        }

        return diff <= step && diff > 0 && oldPosition.getCol() == newPosition.getCol();
    }

    public static Boolean pawnKillMovementValidator(PositionDto oldPosition, PositionDto newPosition, char[][] oldState){
        return oldPosition.getRow() - newPosition.getRow() == 1 && Math.abs(oldPosition.getCol() - newPosition.getCol()) == 1;
    }

    public static boolean isTwoPositionFaceToFaceFlat(char[][] state, PositionDto firstPosition, PositionDto secondPosition, String kingColor) {
        if (firstPosition.getRow() == secondPosition.getRow()) {
            int start = Math.min(firstPosition.getCol(), secondPosition.getCol());
            int end = Math.max(firstPosition.getCol(), secondPosition.getCol());

            for (int col = start + 1; col < end; col++) {
                if (state[firstPosition.getRow()][col] != GameConstants.NONCHARACTER_WALL) {
                    return false;
                }
            }
            return true;
        } else if (firstPosition.getCol() == secondPosition.getCol()) {
            int start = Math.min(firstPosition.getRow(), secondPosition.getRow());
            int end = Math.max(firstPosition.getRow(), secondPosition.getRow());

            for (int row = start + 1; row < end; row++) {
                if (state[row][firstPosition.getCol()] != GameConstants.NONCHARACTER_WALL) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isTwoPositionFaceToFaceDiagonal(char[][] state, PositionDto firstPosition, PositionDto secondPosition, String kingColor) {
        if (firstPosition.getRow() == secondPosition.getRow() || firstPosition.getCol() == secondPosition.getCol()) {
            return false;
        }

        if (Math.abs(firstPosition.getRow() - secondPosition.getRow()) != Math.abs(firstPosition.getCol() - secondPosition.getCol())) {
            return false;
        }

        if (firstPosition.getRow() < secondPosition.getRow()) {
            if (firstPosition.getCol() < secondPosition.getCol()) { // top left to bottom right
                int nRow = firstPosition.getRow() + 1;
                int nCol = firstPosition.getCol() + 1;

                while (nRow < secondPosition.getRow() && nCol < secondPosition.getCol()) {
                    if (state[nRow++][nCol++] != GameConstants.NONCHARACTER_WALL) {
                        return false;
                    }
                }
            } else { // top right to bottom left
                int nRow = firstPosition.getRow() + 1;
                int nCol = firstPosition.getCol() - 1;

                while (nRow < secondPosition.getRow() && nCol > secondPosition.getCol()) {
                    if (state[nRow++][nCol--] != GameConstants.NONCHARACTER_WALL) {
                        return false;
                    }
                }
            }
        } else {
            if (secondPosition.getCol() < firstPosition.getCol()) {  // bottom left to top right
                int nRow = secondPosition.getRow() + 1;
                int nCol = secondPosition.getCol() + 1;

                while (nRow < firstPosition.getRow() && nCol < firstPosition.getCol()) {
                    if (state[nRow++][nCol++] != GameConstants.NONCHARACTER_WALL) {
                        return false;
                    }
                }
            } else { // bottom right to top left
                int nRow = secondPosition.getRow() + 1;
                int nCol = secondPosition.getCol() - 1;

                while (nRow < firstPosition.getRow() && nCol > firstPosition.getCol()) {
                    if (state[nRow++][nCol--] != GameConstants.NONCHARACTER_WALL) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // for [queen, bishop]
    public static Boolean isPositionFacingDiagonalAttacker(PositionDto position, char[][] state, String playerColor){

        // the argument "position" represents the new moving position

        int boardSize = state.length;

        // to bottom right
        int row = position.getRow(); 
        int col = position.getCol();
        while (row < boardSize && col < boardSize){
            char character = state[row++][col++]; 
            if (GameHelper.getDiagonalAttackers(character, playerColor)){
                return true;
            }
        } 

        // to bottom left
        row = position.getRow(); 
        col = position.getCol();
        while (row < boardSize && col >= 0){
            char character = state[row++][col--]; 
            if (GameHelper.getDiagonalAttackers(character, playerColor)){
                return true;
            }
        } 

        // to top right
        row = position.getRow(); 
        col = position.getCol();
        while (row >= 0 && col < boardSize){
            char character = state[row--][col++]; 
            if (GameHelper.getDiagonalAttackers(character, playerColor)){
                return true;
            }
        } 
        // to top left
        row = position.getRow(); 
        col = position.getCol();
        while (row >= 0 && col >= 0){
            char character = state[row--][col--]; 
            if (GameHelper.getDiagonalAttackers(character, playerColor)){
                return true;
            }
        }        
        
        return false;       
    }

        // for [queen, bishop]
        public static ArrayList<Character> getDiagonalAttackerInstances(PositionDto position, char[][] state, String playerColor){

            // the argument "position" represents the new moving position

            ArrayList<Character> attackers = new ArrayList<Character>();
    
            int boardSize = state.length;
    
            // to bottom right
            int row = position.getRow(); 
            int col = position.getCol();
            while (row < boardSize && col < boardSize){
                char character = state[row++][col++];
                String attackerQueenColor = GameHelper.getQueenColor(character);
                if (attackerQueenColor != playerColor){
                      attackers.add(new Queen(attackerQueenColor, new PositionDto(row-1, col-1)));
                      continue;
                } 
                String attackerBishopColor = GameHelper.getBishopColor(character);
                if (attackerBishopColor != playerColor){
                    attackers.add(new Bishop(attackerBishopColor, new PositionDto(row-1, col-1)));
                    continue;
                } 
            } 
    
            // to bottom left
            row = position.getRow(); 
            col = position.getCol();
            while (row < boardSize && col >= 0){
                char character = state[row++][col--]; 
                String attackerQueenColor = GameHelper.getQueenColor(character);
                if (attackerQueenColor != playerColor){
                      attackers.add(new Queen(attackerQueenColor, new PositionDto(row-1, col+1)));
                      continue;
                } 
                String attackerBishopColor = GameHelper.getBishopColor(character);
                if (attackerBishopColor != playerColor){
                    attackers.add(new Bishop(attackerBishopColor, new PositionDto(row-1, col+1)));
                    continue;
                } 
            } 
    
            // to top right
            row = position.getRow(); 
            col = position.getCol();
            while (row >= 0 && col < boardSize){
                char character = state[row--][col++]; 
                String attackerQueenColor = GameHelper.getQueenColor(character);
                if (attackerQueenColor != playerColor){
                      attackers.add(new Queen(attackerQueenColor, new PositionDto(row+1, col-1)));
                      continue;
                } 
                String attackerBishopColor = GameHelper.getBishopColor(character);
                if (attackerBishopColor != playerColor){
                    attackers.add(new Bishop(attackerBishopColor, new PositionDto(row+1, col-1)));
                    continue;
                } 
            } 
            // to top left
            row = position.getRow(); 
            col = position.getCol();
            while (row >= 0 && col >= 0){
                char character = state[row--][col--]; 
                String attackerQueenColor = GameHelper.getQueenColor(character);
                if (attackerQueenColor != playerColor){
                      attackers.add(new Queen(attackerQueenColor, new PositionDto(row+1, col+1)));
                      continue;
                } 
                String attackerBishopColor = GameHelper.getBishopColor(character);
                if (attackerBishopColor != playerColor){
                    attackers.add(new Bishop(attackerBishopColor, new PositionDto(row+1, col+1)));
                    continue;
                } 
            }        
            
            return attackers;       
        }

    // for [queen, rook]
    public static Boolean isPositionFacingFlatDirectionAttacker(PositionDto position, char[][] state, String playerColor){

        // the argument "position" represents the new moving position

        int boardSize = state.length; 

        // to right
        int row = position.getRow(); 
        int col = position.getCol();
        while (col < boardSize){
            char character = state[row][col++]; 
            if (GameHelper.getFlatDirectionAttackers(character, playerColor)){
                return true;
            }
        }

        // to left 
        row = position.getRow();
        col = position.getCol();
        while (col > 0){
            char character = state[row][col--]; 
            if (GameHelper.getFlatDirectionAttackers(character, playerColor)){
                return true;
            }
        }


        // to top 
        row = position.getRow();
        col = position.getCol();
        while (row > 0){
            char character = state[row--][col]; 
            if (GameHelper.getFlatDirectionAttackers(character, playerColor)){
                return true;
            }
        }
        
        // to bottom
        row = position.getRow();
        col = position.getCol();
        while (row < boardSize){
            char character = state[row++][col]; 
            if (GameHelper.getFlatDirectionAttackers(character, playerColor)){
                return true;
            }
        }

        return false;
    }

    // for [queen, rook]
    public static ArrayList<Character> getFlatDirectionAttackerInstances(PositionDto position, char[][] state, String playerColor){

        // the argument "position" represents the new moving position

        ArrayList<Character> attackers = new ArrayList<Character>();

        int boardSize = state.length; 

        // to right
        int row = position.getRow(); 
        int col = position.getCol();
        while (col < boardSize){
            char character = state[row][col++]; 
            String attackerQueenColor = GameHelper.getQueenColor(character);
            if (attackerQueenColor != playerColor){
                  attackers.add(new Queen(attackerQueenColor, new PositionDto(row, col-1)));
                  continue;
            } 
            String attackerRookColor = GameHelper.getRookColor(character);
            if (attackerRookColor != playerColor){
                attackers.add(new Rook(attackerRookColor, new PositionDto(row, col-1)));
                continue;
            }
        }

        // to left 
        row = position.getRow();
        col = position.getCol();
        while (col > 0){
            char character = state[row][col--]; 
            String attackerQueenColor = GameHelper.getQueenColor(character);
            if (attackerQueenColor != playerColor){
                  attackers.add(new Queen(attackerQueenColor, new PositionDto(row, col+1)));
                  continue;
            } 
            String attackerRookColor = GameHelper.getRookColor(character);
            if (attackerRookColor != playerColor){
                attackers.add(new Rook(attackerRookColor, new PositionDto(row, col+1)));
                continue;
            }
        }


        // to top 
        row = position.getRow();
        col = position.getCol();
        while (row > 0){
            char character = state[row--][col]; 
            String attackerQueenColor = GameHelper.getQueenColor(character);
            if (attackerQueenColor != playerColor){
                  attackers.add(new Queen(attackerQueenColor, new PositionDto(row+1, col)));
                  continue;
            } 
            String attackerRookColor = GameHelper.getRookColor(character);
            if (attackerRookColor != playerColor){
                attackers.add(new Rook(attackerRookColor, new PositionDto(row+1, col)));
                continue;
            }
        }
        
        // to bottom
        row = position.getRow();
        col = position.getCol();
        while (row < boardSize){
            char character = state[row++][col]; 
            String attackerQueenColor = GameHelper.getQueenColor(character);
            if (attackerQueenColor != playerColor){
                  attackers.add(new Queen(attackerQueenColor, new PositionDto(row-1, col)));
                  continue;
            } 
            String attackerRookColor = GameHelper.getRookColor(character);
            if (attackerRookColor != playerColor){
                attackers.add(new Rook(attackerRookColor, new PositionDto(row-1, col)));
                continue;
            }
        }

        return attackers;
    }    

    public static Boolean isPositionCoveredByEnemyKnight(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        List<BiFunction<Integer, Integer, PositionDto>> knightAttackList = new ArrayList<>();

        knightAttackList.add((row, col) -> new PositionDto(row + 2, col + 1));
        knightAttackList.add((row, col) -> new PositionDto(row + 2, col - 1));
        knightAttackList.add((row, col) -> new PositionDto(row - 2, col + 1));
        knightAttackList.add((row, col) -> new PositionDto(row - 2, col - 1));
        knightAttackList.add((row, col) -> new PositionDto(row + 1, col + 2));
        knightAttackList.add((row, col) -> new PositionDto(row + 1, col - 2));
        knightAttackList.add((row, col) -> new PositionDto(row - 1, col + 2));
        knightAttackList.add((row, col) -> new PositionDto(row - 1, col - 2));

        for (BiFunction<Integer, Integer, PositionDto> validator : knightAttackList){
            PositionDto pos = validator.apply(position.getRow(), position.getCol());
            if (pos.getRow() >= boardSize || pos.getCol() >= boardSize || pos.getRow() < 0 || pos.getCol() < 0){
                continue;
            }

            String knightColor = GameHelper.getKnightColor(state[pos.getRow()][pos.getCol()]);
            if (knightColor != null && knightColor != playerColor){
                return true;
            }
        }

        return false;
    }

    // for knight, we do not use array to get knight attackers because it is not possible to have multiple knight attackers.
    public static Character getKnightAttackerInstance(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        List<BiFunction<Integer, Integer, PositionDto>> knightAttackList = new ArrayList<>();

        knightAttackList.add((row, col) -> new PositionDto(row + 2, col + 1));
        knightAttackList.add((row, col) -> new PositionDto(row + 2, col - 1));
        knightAttackList.add((row, col) -> new PositionDto(row - 2, col + 1));
        knightAttackList.add((row, col) -> new PositionDto(row - 2, col - 1));
        knightAttackList.add((row, col) -> new PositionDto(row + 1, col + 2));
        knightAttackList.add((row, col) -> new PositionDto(row + 1, col - 2));
        knightAttackList.add((row, col) -> new PositionDto(row - 1, col + 2));
        knightAttackList.add((row, col) -> new PositionDto(row - 1, col - 2));

        for (BiFunction<Integer, Integer, PositionDto> validator : knightAttackList){
            PositionDto pos = validator.apply(position.getRow(), position.getCol());
            if (pos.getRow() >= boardSize || pos.getCol() >= boardSize || pos.getRow() < 0 || pos.getCol() < 0){
                continue;
            }
            if (GameHelper.getKnightColor(state[pos.getRow()][pos.getCol()]) != playerColor){
                return new Knight(GameHelper.getPieceColor(state[pos.getRow()][pos.getCol()]), new PositionDto(pos.getRow(), pos.getCol()));
            }
        }

        return null;
    }

    public static Boolean isPositionCoveredByEnemyPawn(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        if (position.getRow() - 1 >= boardSize || position.getCol() + 1 >= boardSize || position.getRow() - 1 < 0 || position.getCol() + 1 < 0){
            return false;
        }
        return GameHelper.getPawnColor(state[position.getRow() - 1][position.getCol() + 1]) != null || GameHelper.getPawnColor(state[position.getRow() - 1][position.getCol() - 1]) != null;
    }

    // for pawn, we do not use array to get knight attackers because it is not possible to have multiple pawn attackers.
    public static Character getPawnAttackerInstance(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        
        // index out of bounds
        if (position.getRow() - 1 >= boardSize || position.getCol() + 1 >= boardSize || position.getRow() - 1 < 0 || position.getCol() + 1 < 0){
            return null;
        }

        if (GameHelper.getPawnColor(state[position.getRow() - 1][position.getCol() + 1]) != playerColor){
            return new Pawn(GameHelper.getPieceColor(state[position.getRow() - 1][position.getCol() + 1]), new PositionDto(position.getRow() - 1, position.getCol() + 1));
        }
        
        if (GameHelper.getPawnColor(state[position.getRow() - 1][position.getCol() - 1]) != playerColor){
            return new Pawn(GameHelper.getPieceColor(state[position.getRow() - 1][position.getCol() - 1]), new PositionDto(position.getRow() - 1, position.getCol() - 1));
        }

        return null;
    }    

    public static Boolean isPositionCoveredByEnemyKing(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        List<BiFunction<Integer, Integer, PositionDto>> enemyKingAttackList = new ArrayList<>();

        enemyKingAttackList.add((row, col) -> new PositionDto(row + 1, col + 1));
        enemyKingAttackList.add((row, col) -> new PositionDto(row + 1, col - 1));
        enemyKingAttackList.add((row, col) -> new PositionDto(row - 1, col + 1));
        enemyKingAttackList.add((row, col) -> new PositionDto(row - 1, col - 1));
        enemyKingAttackList.add((row, col) -> new PositionDto(row + 1, col));
        enemyKingAttackList.add((row, col) -> new PositionDto(row - 1, col));
        enemyKingAttackList.add((row, col) -> new PositionDto(row, col + 1));
        enemyKingAttackList.add((row, col) -> new PositionDto(row, col - 1));

        for (BiFunction<Integer, Integer, PositionDto> validator : enemyKingAttackList){
            PositionDto pos = validator.apply(position.getRow(), position.getCol());
            if (pos.getRow() >= boardSize || pos.getCol() >= boardSize || pos.getRow() < 0 || pos.getCol() < 0){
                continue;
            }

            String enemyKingColor = GameHelper.getKingColor(state[pos.getRow()][pos.getCol()]); 
            if (enemyKingColor != null && enemyKingColor != playerColor){
                return true;
            }
        }

        return false;
    }

    // get king attacker instance is not implemented because it is impossible for two kings to collide

    public static Boolean isPositionCoveredByEnemyEvolvedPawn(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        List<BiFunction<Integer, Integer, PositionDto>> enemyEvolvedPawnAttackList = new ArrayList<>();

        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 1, col + 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 1, col - 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 1, col + 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 1, col - 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 1, col));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 1, col));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row, col + 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row, col - 1));

        for (BiFunction<Integer, Integer, PositionDto> validator : enemyEvolvedPawnAttackList){
            PositionDto pos = validator.apply(position.getRow(), position.getCol());
            if (pos.getRow() >= boardSize || pos.getCol() >= boardSize || pos.getRow() < 0 || pos.getCol() < 0){
                continue;
            }

            String evolvedPawnColor = GameHelper.getEvolvedPawnColor(state[pos.getRow()][pos.getCol()]);
            if (evolvedPawnColor != null && evolvedPawnColor != playerColor){
                return true;
            }
        }

        return false;
    }

    public static Character getEvolvedPawnAttackerInstance(PositionDto position, char[][] state, String playerColor){
        int boardSize = state.length;
        List<BiFunction<Integer, Integer, PositionDto>> enemyEvolvedPawnAttackList = new ArrayList<>();

        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 2, col + 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 2, col - 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 2, col + 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 2, col - 1));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 1, col + 2));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row + 1, col - 2));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 1, col + 2));
        enemyEvolvedPawnAttackList.add((row, col) -> new PositionDto(row - 1, col - 2));

        for (BiFunction<Integer, Integer, PositionDto> validator : enemyEvolvedPawnAttackList){
            PositionDto pos = validator.apply(position.getRow(), position.getCol());
            if (pos.getRow() >= boardSize || pos.getCol() >= boardSize || pos.getRow() < 0 || pos.getCol() < 0){
                continue;
            }
            if (GameHelper.getKingColor(state[pos.getRow()][pos.getCol()]) != playerColor){
                return new EvolvedPawn(GameHelper.getPieceColor(state[pos.getRow()][pos.getCol()]), pos);
            }
        }

        return null;
    }

    public static Boolean isPositionBetweenTwoPositionDiagonally(PositionDto targetPosition, PositionDto firstPosition, PositionDto secondPosition){

        int rowUpperBound = Math.max(firstPosition.getRow(), secondPosition.getRow());
        int rowLowerBound = Math.min(firstPosition.getRow(), secondPosition.getRow());

        int colUpperBound = Math.max(firstPosition.getCol(), secondPosition.getCol());
        int colLowerBound = Math.min(firstPosition.getCol(), secondPosition.getCol());

        return targetPosition.getRow() > rowLowerBound && targetPosition.getRow() < rowUpperBound && 
            targetPosition.getCol() > colLowerBound && targetPosition.getCol() < colUpperBound && 
            diagonalMovementValidator(firstPosition, secondPosition, null);
    }

    public static Boolean isPositionBetweenTwoPositionFlatly(PositionDto targetPosition, PositionDto firstPosition, PositionDto secondPosition){

        int rowUpperBound = Math.max(firstPosition.getRow(), secondPosition.getRow());
        int rowLowerBound = Math.min(firstPosition.getRow(), secondPosition.getRow());

        int colUpperBound = Math.max(firstPosition.getCol(), secondPosition.getCol());
        int colLowerBound = Math.min(firstPosition.getCol(), secondPosition.getCol());

        return (
                (targetPosition.getRow() > rowLowerBound && targetPosition.getRow() < rowUpperBound) ||
                (targetPosition.getCol() > colLowerBound && targetPosition.getCol() < colUpperBound)
            ) && 
            straightMovementValidator(firstPosition, secondPosition, null);
    }

    public static Boolean isPositionGuarded(PositionDto position, char[][] state, String playerColor){
        Boolean longRangeAttackersGuard = MovementUsecase.isPositionFacingDiagonalAttacker(position, state, playerColor) || MovementUsecase.isPositionFacingFlatDirectionAttacker(position, state, playerColor);

        Boolean knightAttackersGuard = MovementUsecase.isPositionCoveredByEnemyKnight(position, state, playerColor);
        Boolean pawnAttackersGuard = MovementUsecase.isPositionCoveredByEnemyPawn(position, state, playerColor);
        Boolean evolvedPawnAttackersGuard = MovementUsecase.isPositionCoveredByEnemyEvolvedPawn(position, state, playerColor);
        Boolean enemyKingGuard = MovementUsecase.isPositionCoveredByEnemyKing(position, state, playerColor);
        return longRangeAttackersGuard || knightAttackersGuard || pawnAttackersGuard || evolvedPawnAttackersGuard || enemyKingGuard;
    }

}