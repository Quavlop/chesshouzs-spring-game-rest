package com.chesshouzs.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.Helper;
import com.chesshouzs.server.utils.DoubleMovementScanResultTestSuite;
import com.chesshouzs.server.utils.IsValidMoveTestSuite;

@SpringBootTest
public class MoveValidationModuleTests {

    // TODO : eat friends, double eat friends, replace walls position on eat / forward, eat enemy, double eat enemy
    @Test 
    void DoubleMovementScanResultTest() throws Exception {
    
        DoubleMovementScanResultTestSuite[] tests = {
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : single movement from higher board index to lower board index", 
                "......k....R..|..R...........|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.BLACK_CHARACTER_KING);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(0, 6));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(2, 0));
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : single movement from lower board index to higher board index", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "k..........R..|..R...........|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.BLACK_CHARACTER_KING);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(2, 0));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(0, 0));
                }}
            ),    
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : single movement from lower board index to higher board index (2)", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "...........R..|..R...........|k.............|...........K.N|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.WHITE_CHARACTER_KNIGHT);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(2, 9));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(3, 13));
                }}
            ),              
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double movement (1)", 
                "...........R..|........R.....|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "k..........R..|R.............|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double movement (2)", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "k..........R..|..R...........|........N.....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),                               
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double movement (3)", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
        };

        int countFail = 0;

        for (DoubleMovementScanResultTestSuite test : tests){
            char [][] oldStateArgToArray = GameHelper.convertNotationToArray(test.getOldStateArg());
            char [][] newStateArgToArray = GameHelper.convertNotationToArray(test.getNewStateArg());
            Map<String, Object> result = Game.doubleMovementScanResult(oldStateArgToArray, newStateArgToArray);

            Boolean isComparisonResultEqual = Helper.compareJson(test.getExpectation(), result);
            if (!isComparisonResultEqual){
                countFail += 1;
            }

            assertEquals(
                isComparisonResultEqual,
                true,
                String.format("[FAILED TEST] MoveValidationModuleTests.DoubleMovementScanResultTest : %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              Helper.convertObjectToJson(test.getExpectation()), 
                              Helper.convertObjectToJson(result)
                            )
            );
        }

        if (countFail <= 0){
            System.out.println("==== PASSED ALL TEST CASE ====");
        } else {
            System.out.println("==== FAILED ====");
        }
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.DoubleMovementScanResultTest : %s / %s tests passed", tests.length - countFail, tests.length));

    }

    // TODO : movement againts wall & friends (on single & double step), eat friends & wall on left and right, 
    // checking is done on player-POV-wise. By default on db, black is on top, therefore board is flipped if player is black color
    @Test 
    void PawnIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {
            new IsValidMoveTestSuite(
                "POSITIVE CASE : Pawn forward movement (not on starting position)", 
                "..............|..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : Pawn forward movement double step ON STARTING POSITION", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : Pawn forward movement single step ON STARTING POSITION", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : Pawn kill movement to left on enemy", 
                "............N.|.............p|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : Pawn kill movement to right on enemy", 
                ".............R|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".............p|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                        
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn kill movement to left on empty position", 
                "..............|.............p|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                                   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement but backwards (black)", 
                "..............|..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement but backwards (white)", 
                "..............|..............|............P.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|............P.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement double step (not on starting position)", 
                "..............|..............|............P.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "............P.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                                             
        };

        int countFail = 0;
        for (IsValidMoveTestSuite test : tests){
            char [][] oldStateArgToArray = GameHelper.convertNotationToArray(test.getOldStateArg());
            char [][] newStateArgToArray = GameHelper.convertNotationToArray(test.getNewStateArg());

            Map<String, Object> movement = Game.getMovementData(oldStateArgToArray, newStateArgToArray);

            Boolean isNotDoubleMovement = (Boolean)movement.get(GameConstants.KEY_VALID_MOVE);
            assertEquals(
                isNotDoubleMovement, 
                true, 
                String.format("[FAILED TEST] MoveValidationModuleTests.CharacterIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );

            Character character = (Character)movement.get(GameConstants.KEY_CHARACTER);
            System.out.println("LOL " + character.getPosition().getRow() + " " + character.getPosition().getCol());
            
            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.CharacterIsValidMove : %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              result, 
                              test.getExpectation()
                            )                     
            );
        }

        if (countFail <= 0){
            System.out.println("==== PASSED ALL TEST CASE ====");
        } else {
            System.out.println("==== FAILED ====");
        }
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.CharacterIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));        
    }
}
