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

    // Attack againsts wall or teammate is not done here (implemented on Character.isValidMove)
    @Test 
    void DoubleMovementScanResultTest() throws Exception {
    
        DoubleMovementScanResultTestSuite[] tests = {
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : single movement from higher board index to lower board index", 
                "......k....R..|..R...........|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "...........R..|..R...........|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, false); 
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
                    put(GameConstants.KEY_INVALID, false); 
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
                    put(GameConstants.KEY_INVALID, false); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.WHITE_CHARACTER_KNIGHT);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(2, 9));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(3, 13));
                }}
            ),   
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : white eat black", 
                "...Q.........n|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                ".............Q|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, false); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.WHITE_CHARACTER_QUEEN);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(0, 3));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(0, 13));
                }}
            ),     
            new DoubleMovementScanResultTestSuite(
                "POSITIVE CASE : black eat white", 
                "..............|............b.|.............Q|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..............|.............b|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, false); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, GameConstants.BLACK_CHARACTER_BISHOP);
                    put(GameConstants.KEY_OLD_POSITION, new PositionDto(1, 12));
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(2, 13));
                }}
            ),                                     
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double movement (1)", 
                "...........R..|........R.....|k........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "k..........R..|R.............|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
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
                    put(GameConstants.KEY_INVALID, true); 
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
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double eat enemy (attacker from same color)", 
                "...........rR.|...........Rr.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",

                "...........R..|............R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),       
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : double eat enemy (attacker from different color - black eat white AND vice versa)", 
                "...........rR.|...........Rr.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",

                "............r.|............R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),                   

            // test case for random state altercation
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : remove all piece", 
                "...........rR.|...........Rr.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : remove several piece (1)", 
                "...........rR.|...........Rr.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "...........r..|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : remove several piece (2)", 
                "...........rR.|...........Rr.|.......kQ.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "...........r..|............r.|........Q.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),    
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : remove one piece", 
                "...........rR.|...........Rr.|.......kQn....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "...........rR.|...........Rr.|.......kQ.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),              
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : adding piece (1)", 
                "...........rR.|...........Rr.|.......kQ.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|qqqqqq........|..............|...rrrr.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : adding piece (2)", 
                "...........rR.|...........Rr.|.......kQ.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "...........rR.|...........Rr.|.......kQn....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
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
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement and collide with wall", 
                "..............|............0.|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),     
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn kill movement and collide with left wall", 
                "..............|...........0..|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...........p..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn kill movement and collide with right wall", 
                "..............|.............0|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............p|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn on default position and try to bypass wall ahead", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.............0|.............p|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.............p|.............0|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement and collide with friend", 
                "..............|............n.|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),          
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn forward movement and collide with enemy", 
                "..............|............N.|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn kill friend on left", 
                "..............|...........n..|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...........p..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),        
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn kill friend on right", 
                "..............|.k...........n|............p.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.k...........p|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement collide with wall (took double step)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.0............|..............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement collide with friend (took double step)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.r............|..............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement collide with enemy (took double step)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.R............|..............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement bypassing (jumpover) wall", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.0............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|.0............|..............|..............|..............|", 
                false
            ),              
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement bypassing (jumpover) friend", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.r............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|.r............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : Pawn double movement bypassing (jumpover) friend", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.R............|.p............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.p............|.R............|..............|..............|..............|", 
                false
            ),                                                                                                                                                                                                                      
        };

        int countFail = 0;
        for (IsValidMoveTestSuite test : tests){
            char [][] oldStateArgToArray = GameHelper.convertNotationToArray(test.getOldStateArg());
            char [][] newStateArgToArray = GameHelper.convertNotationToArray(test.getNewStateArg());

            Map<String, Object> movement = Game.getMovementData(oldStateArgToArray, newStateArgToArray);

            Boolean isNotDoubleMovement = (Boolean)movement.get(GameConstants.KEY_VALID_MOVE);
            Character character = (Character)movement.get(GameConstants.KEY_CHARACTER);            

            assertEquals(
                isNotDoubleMovement, 
                true, 
                String.format("[FAILED TEST] MoveValidationModuleTests.CharacterIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
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

    @Test 
    void KingIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            )   
        };

        int countFail = 0;
        for (IsValidMoveTestSuite test : tests){
            char [][] oldStateArgToArray = GameHelper.convertNotationToArray(test.getOldStateArg());
            char [][] newStateArgToArray = GameHelper.convertNotationToArray(test.getNewStateArg());

            Map<String, Object> movement = Game.getMovementData(oldStateArgToArray, newStateArgToArray);

            Boolean isNotDoubleMovement = (Boolean)movement.get(GameConstants.KEY_VALID_MOVE);
            Character character = (Character)movement.get(GameConstants.KEY_CHARACTER);            

            assertEquals(
                isNotDoubleMovement, 
                true, 
                String.format("[FAILED TEST] MoveValidationModuleTests.CharacterIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
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
