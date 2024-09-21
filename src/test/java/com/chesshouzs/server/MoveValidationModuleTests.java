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
                "NEGATIVE CASE : replacing a piece", 
                "...........R..|..............|.........N....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "...........R..|..............|.........E....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : replacing two piece", 
                "...........R..|..............|.........N....|...........r..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|", 
                "...........R..|..............|.........E....|...........K..|..............|.............r|..............|..............|..............|..............|..............|..............|..............|.............r|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
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
            
            
            // NEGATIVE CASE : corner cases 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : two queen, left with one queen (one is replaced by another OR removed)", 
                "..............|..............|..q...........|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),      
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : move but changed color", 
                "..............|..............|..q...........|.qQ...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..............|..............|.q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : swap position", 
                "..............|..............|..q...........|.Q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..............|..Q...........|.q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),             
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : two queen, one changing color", 
                "..............|..............|..Q...........|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..q...........|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, true); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),                                            
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : contains invalid character (1)", 
                "..............|..............|..Q...,.......|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..q...........|..Q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ), 
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : contains invalid character (2)", 
                ".........w....|..............|..Q...,.......|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "..............|..q...........|..Q....x......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                new HashMap<String, Object>(){{
                    put(GameConstants.KEY_INVALID, true); 
                    put(GameConstants.KEY_IS_DOUBLE, false); 
                    put(GameConstants.KEY_CHARACTER, '\u0000');
                    put(GameConstants.KEY_OLD_POSITION, null);
                    put(GameConstants.KEY_NEW_POSITION, null);
                }}
            ),
            new DoubleMovementScanResultTestSuite(
                "NEGATIVE CASE : contains invalid character (3)", 
                "..............|..............|..Q...,.......|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
                "....LMAO......|..q...........|..Q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|",
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
                String.format("[FAILED TEST] MoveValidationModuleTests.PawnIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.PawnIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.PawnIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));        
    }

    // Check status movement test cases will be implemented on different test module.
    @Test 
    void KingIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {
            // POSITIVE CASE : regular movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to right", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to left", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top right (diagonal)", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top left (diagonal)", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom right (diagonal)", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom left (diagonal)", 
                "..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),     
            
            
            // POSITIVE CASE : kill enemy
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top and kill enemy", 
                "...N..........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom and kill enemy", 
                "..............|...k..........|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to right and kill enemy", 
                "..............|...kQ.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to left and kill enemy", 
                "..............|..Pk..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top right (diagonal) and kill enemy", 
                "....N.........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to top left (diagonal) and kill enemy", 
                "..N...........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom right (diagonal) and kill enemy", 
                "..............|...k..........|....R.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : black king move to bottom left (diagonal) and kill enemy", 
                "..............|...k..........|..R...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),



            
            // NEGATIVE CASE for movement out of bounds
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top (2 step)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom (2 step)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to right (2 step)", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to left (2 step)", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|k.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top right (diagonal, 2 step)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top left (diagonal, 2 step)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "k.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom right (diagonal, 2 step)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom left (diagonal)", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|k.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            
            // NEGATIVE CASE for killing friends
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top and kill friend", 
                "...n..........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom and kill friend", 
                "..............|...k..........|...q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to right and kill friend", 
                "..............|...kq.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to left and kill friend", 
                "..............|..pk..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top right (diagonal) and kill friend", 
                "....n.........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top left (diagonal) and kill friend", 
                "..n...........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom right (diagonal) and kill friend", 
                "..............|...k..........|....r.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom left (diagonal) and kill friend", 
                "..............|...k..........|..r...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 

            // NEGATIVE CASE for override wall position
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top and kill wall", 
                "...0..........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom and kill wall", 
                "..............|...k..........|...0..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to right and kill wall", 
                "..............|...k0.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to left and kill wall", 
                "..............|..0k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top right (diagonal) and kill wall", 
                "....0.........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to top left (diagonal) and kill wall", 
                "..0...........|...k..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom right (diagonal) and kill wall", 
                "..............|...k..........|....0.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....k.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : black king move to bottom left (diagonal) and kill wall", 
                "..............|...k..........|..0...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..k...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.KingIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.KingIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.KingIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));   
    }

    @Test
    void RookIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {

            // regular movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook move to right", 
                "..............|...R..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............R|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook move to left", 
                "..............|...R..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook move to top", 
                "..............|..............|..............|R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook move to bottom", 
                "R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|R.............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over wall (vertical)", 
                "r.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over wall (horizontal)", 
                "r.....0.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....r0.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over friend (vertical)", 
                "r.............|..............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|r.............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over friend (horizontal)", 
                "r.....n.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....rn.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over enemy (vertical)", 
                "r.............|..............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|r.............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : rook does not jump over enemy (horizontal)", 
                "r.....N.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....rN.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                                       
            
            
            // kill movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy on horizontal", 
                "..............|...r..........|..............|r........Q....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|.........r....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy on vertical", 
                "..............|...r..........|N.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|r.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),            

            // NEGATIVE CASE : out of scope movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (1)", 
                "..............|..............|..............|r.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (2)", 
                "..............|...r......n...|r.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r......n...|..............|.....r........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (3)", 
                "..............|..............|..............|..............|........R.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|.........R....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                                


            // NEGATIVE CASE : kill friend movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend on horizontal", 
                "..............|...r..........|..............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|.........r....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend on vertical", 
                "..............|...r..........|n.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|r.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              

            // NEGATIVE CASE : override wall movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on horizontal", 
                "..............|...r..........|..............|r........0....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|.........r....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on vertical", 
                "..............|...r..........|0.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|r.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              
            
            // NEGATIVE CASE : jump over friend, enemy, wall
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (vertical) (1)", 
                "r.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|0.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (vertical) (2)", 
                "..............|..............|0.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (horizontal) (1)", 
                "r.......0.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0....r|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (horizontal) (2)", 
                "........0.....|..........0..r|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........r0...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),         
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (vertical) (1)", 
                "r.............|..............|n.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|n.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (vertical) (2)", 
                "..............|..............|e.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (horizontal) (1)", 
                "r.......q.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........q....r|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (horizontal) (2)", 
                "........0.....|..........b..r|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........rb...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (vertical) (1)", 
                "r.............|..............|N.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|N.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (vertical) (2)", 
                "..............|..............|E.............|r........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (horizontal) (1)", 
                "r.......Q.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........Q....r|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (horizontal) (2)", 
                "........0.....|..........B..r|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........rB...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.RookIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.RookIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.RookIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));   
    }

    @Test
    void BishopIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {
            // regular movement
            new IsValidMoveTestSuite(
            "POSITIVE CASE : bishop move to top-right", 
            "..............|..............|.....B........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
            ".......B......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
            true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop move to top-left", 
                "..............|..............|.....B........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...B..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop move to bottom-right", 
                "..............|..............|..............|B.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|...B..........|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop move to bottom-left", 
                "..............|..............|..............|..............|..............|.............B|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.........B....|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over wall (top-right)", 
                ".....0........|..............|...B..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....0........|....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over wall (top-left)", 
                ".0............|..............|..............|....b.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".0............|..b...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over friend (bottom-right)", 
                "..............|b.............|..............|..............|..............|....n.........|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..b...........|..............|....n.........|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over friend (bottom-left)", 
                "..............|..............|.............b|..............|..............|..........e...|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|...........b..|..........e...|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over enemy (top-right)", 
                "..Q...........|..............|b.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..Q...........|.b............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : bishop does not jump over enemy (bottom-left)", 
                "..............|..............|.............b|..............|..............|..............|.........R....|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|...........b..|..............|.........R....|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                                       
            
            
            // kill movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy (top-right)", 
                "....r.........|..............|..B...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy  (bottom-right)", 
                "..............|..............|...b..........|..............|..............|......E.......|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|......b.......|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy  (bottom-left)", 
                "..............|..............|.B............|..b...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|.b............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                       

            // NEGATIVE CASE : out of scope movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (1)", 
                "...........N..|.............b|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...........b..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (2)", 
                "....b.........|r.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....b........|r.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (3)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|b.............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|....b.........|", 
                false
            ),                                


            // NEGATIVE CASE : kill friend movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend (top-left)", 
                "R.............|..............|..B...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "B.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend (bottom-right)", 
                "..............|...r..........|r.............|.........n....|B.............|.E............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|r.............|.........n....|..............|.B............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              

            // NEGATIVE CASE : override wall movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall (top-left)", 
                "0.............|..............|..B...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "B.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on (bottom-right)", 
                "..............|...r..........|0.............|.B.......n....|..0...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|0.............|.........n....|..B...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              
            
            // NEGATIVE CASE : jump over friend, enemy, wall
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (top-left)", 
                "..............|..............|00............|..B......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|B.............|00............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (bottom-left)", 
                "r.............|..............|0............B|r........n..0.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..0.|...........B..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                 
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (top-left)", 
                "..............|..............|QR............|..B......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|B.............|QR............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (bottom-left)", 
                "r.............|..............|0............B|r........n..R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..R.|...........B..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (top-left) (1)", 
                "..............|...P..........|....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..B...........|...P..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (bottom-right) (2)", 
                "..B...........|...P..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...P..........|....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (top-left)", 
                "..............|..............|Qr............|..B......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|B.............|Qr............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (bottom-left)", 
                "r.............|..............|0............B|r........n..r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..r.|...........B..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (top-left) (1)", 
                "..............|...q..........|....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..B...........|...q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (bottom-right) (2)", 
                "..B...........|...b..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...b..........|....B.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.BishopIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.BishopIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.BishopIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));           
    }

    @Test
    void QueenIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {

            // FLAT DIRECTION MOVEMENT 
            // regular movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to right", 
                "..............|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............Q|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to left", 
                "..............|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to top", 
                "..............|..............|..............|Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to bottom", 
                "R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|R.............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over wall (vertical)", 
                "Q.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|Q.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over wall (horizontal)", 
                "Q.....0.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....Q0.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over friend (vertical)", 
                "r.............|..............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|r.............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over friend (horizontal)", 
                "q.....n.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....qn.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over enemy (vertical)", 
                "q.............|..............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|q.............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over enemy (horizontal)", 
                "q.....N.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....qN.......|r.............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                                       
            
            
            // kill movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy on horizontal", 
                "..............|...q..........|..............|q........Q....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...q..........|..............|.........q....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy on vertical", 
                "..............|...r..........|N.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|q.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),            

            // NEGATIVE CASE : out of scope movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (1)", 
                "..............|..............|..............|Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (2)", 
                "..............|...r......n...|q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r......n...|..............|.....q........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (3)", 
                "..............|..............|..............|..............|........Q.....|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                                


            // NEGATIVE CASE : kill friend movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend on horizontal", 
                "..............|...r..........|..............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|.........q....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend on vertical", 
                "..............|...r..........|n.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|q.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              

            // NEGATIVE CASE : override wall movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on horizontal", 
                "..............|...r..........|..............|Q........0....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|..............|.........Q....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on vertical", 
                "..............|...r..........|0.............|R........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|R.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              
            
            // NEGATIVE CASE : jump over friend, enemy, wall
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (vertical) (1)", 
                "Q.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|0.............|Q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (vertical) (2)", 
                "..............|..............|0.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "q.............|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (horizontal) (1)", 
                "Q.......0.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0....Q|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (horizontal) (2)", 
                "........0.....|..........0..q|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........q0...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),         
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (vertical) (1)", 
                "q.............|..............|n.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|n.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (vertical) (2)", 
                "..............|..............|e.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "q.............|..............|e.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (horizontal) (1)", 
                "Q.......Q.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........Q....Q|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (horizontal) (2)", 
                "........0.....|..........b..q|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........qb...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (vertical) (1)", 
                "q.............|..............|N.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|N.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (vertical) (2)", 
                "..............|..............|E.............|q........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "q.............|..............|E.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (horizontal) (1)", 
                "q.......Q.....|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........Q....q|..............|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (horizontal) (2)", 
                "........0.....|..........B..q|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........0.....|.........qB...|0.............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                         


            // DIAGONAL DIRECTION MOVEMENT
           // regular movement
           new IsValidMoveTestSuite(
            "POSITIVE CASE : queen move to top-right", 
            "..............|..............|.....Q........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
            ".......Q......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
            true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to top-left", 
                "..............|..............|.....Q........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to bottom-right", 
                "..............|..............|..............|Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|...Q..........|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen move to bottom-left", 
                "..............|..............|..............|..............|..............|.............Q|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|.........Q....|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over wall (top-right)", 
                ".....0........|..............|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".....0........|....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over wall (top-left)", 
                ".0............|..............|..............|....q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".0............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over friend (bottom-right)", 
                "..............|q.............|..............|..............|..............|....n.........|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..q...........|..............|....n.........|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over friend (bottom-left)", 
                "..............|..............|.............Q|..............|..............|..........e...|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|...........Q..|..........e...|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over enemy (top-right)", 
                "..Q...........|..............|q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..Q...........|.q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),        
            new IsValidMoveTestSuite(
                "POSITIVE CASE : queen does not jump over enemy (bottom-left)", 
                "..............|..............|.............Q|..............|..............|..............|.........R....|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|...........Q..|..............|.........R....|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                                       
            
            
            // kill movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy (top-right)", 
                "....r.........|..............|..Q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy  (bottom-right)", 
                "..............|..............|...q..........|..............|..............|......E.......|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..............|......q.......|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : kill enemy  (bottom-left)", 
                "..............|..............|.B............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|.q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),                       

            // NEGATIVE CASE : out of scope movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (1)", 
                "...........N..|.............q|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...........q..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (2)", 
                "....q.........|r.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|r.............|q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of scope movement (3)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|q.............|..............|", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|....q.........|", 
                false
            ),                                


            // NEGATIVE CASE : kill friend movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend (top-left)", 
                "R.............|..............|..Q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : kill friend (bottom-right)", 
                "..............|...r..........|r.............|.........n....|Q.............|.E............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|r.............|.........n....|..............|.Q............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              

            // NEGATIVE CASE : override wall movement
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall (top-left)", 
                "0.............|..............|..Q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "Q.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : override wall on (bottom-right)", 
                "..............|...r..........|0.............|.q.......n....|..0...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...r..........|0.............|.........n....|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              
            
            // NEGATIVE CASE : jump over friend, enemy, wall
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (top-left)", 
                "..............|..............|00............|..q......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|q.............|00............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over wall (bottom-left)", 
                "r.............|..............|0............Q|r........n..0.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..0.|...........Q..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                 
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (top-left)", 
                "..............|..............|QR............|..Q......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|Q.............|QR............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (bottom-left)", 
                "r.............|..............|0............Q|r........n..R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..R.|...........Q..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (top-left) (1)", 
                "..............|...P..........|....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..Q...........|...P..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over friend (bottom-right) (2)", 
                "..Q...........|...P..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...P..........|....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (top-left)", 
                "..............|..............|Qr............|..Q......n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|Q.............|Qr............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (bottom-left)", 
                "r.............|..............|0............Q|r........n..r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "r.............|..............|0.............|r........n..r.|...........Q..|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (top-left) (1)", 
                "..............|...q..........|....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..Q...........|...q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : jump over enemy (bottom-right) (2)", 
                "..Q...........|...b..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...b..........|....Q.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.QueenIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.QueenIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.QueenIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));          
    }


    @Test 
    void KnightIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {

            // regular movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +2 row +1 col", 
                "...........N..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|............N.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +2 row -1 col", 
                "...........N..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..........N...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -2 row +1 col", 
                "..............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...N..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -2 row -1 col", 
                "..............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".N............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            
            
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +1 row +2 col", 
                "...........N..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............N|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +1 row -2 col", 
                "...........n..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -1 row +2 col", 
                "..............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -1 row -2 col", 
                "..............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....N.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),             

            // POSITIVE CASE : kill enemy 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +2 row +1 col (kill enemy)", 
                "...........N..|..............|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|............N.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +2 row -1 col (kill enemy)", 
                "...........N..|..............|..........b...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..........N...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -2 row +1 col (kill enemy)", 
                "...e..........|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...N..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -2 row -1 col (kill enemy)", 
                ".q............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".N............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            
            
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +1 row +2 col (kill enemy)", 
                "...........N..|.............b|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............N|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),    
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move +1 row -2 col (kill enemy)", 
                "...........n..|.........N....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -1 row +2 col (kill enemy)", 
                "..............|n.............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ), 
            new IsValidMoveTestSuite(
                "POSITIVE CASE : move -1 row -2 col (kill enemy)", 
                "..............|....r.........|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....N.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),             
            
            // NEGATIVE CASE : kill friend 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +2 row +1 col (kill friend)", 
                "...........N..|..............|............R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|............N.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +2 row -1 col (kill friend)", 
                "...........N..|..............|..........B...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..........N...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -2 row +1 col (kill friend)", 
                "...E..........|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...N..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -2 row -1 col (kill friend)", 
                ".Q............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".N............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +1 row +2 col (kill friend)", 
                "...........N..|.............B|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.............N|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +1 row -2 col (kill friend)", 
                "...........n..|.........p....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -1 row +2 col (kill friend)", 
                "..............|E.............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -1 row -2 col (kill friend)", 
                "..............|....Q.........|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....N.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),              

            // NEGATIVE CASE : override wall position
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +2 row +1 col (override wall)", 
                "...........N..|..............|............0.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|............N.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +2 row -1 col (override wall)", 
                "...........N..|..............|..........0...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..........N...|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -2 row +1 col (override wall)", 
                "...0..........|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...N..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -2 row -1 col (override wall)", 
                ".0............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                ".N............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            
            
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +1 row +2 col (override wall)", 
                "...........N..|............00|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|............0N|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move +1 row -2 col (override wall)", 
                "...........n..|.........0....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.........n....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -1 row +2 col (override wall)", 
                "..............|0.............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : move -1 row -2 col (override wall)", 
                "..............|...N0N........|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|...NNN........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            
            // NEGATIVE CASE : knight out of bound movements
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of bound movement (1)", 
                "..............|..............|..N...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of bound movement (2)", 
                "..............|..............|..n...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...n..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : out of bound movement (3)", 
                "..............|..............|......N.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|N.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.KnightIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.KnightIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.KnightIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));                 

    }

    @Test 
    void EvolvedPawnIsValidMoveTest() throws Exception {
        IsValidMoveTestSuite[] tests = {
            // POSITIVE CASE : regular movement
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top", 
                "..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom", 
                "..............|...E..........|........Ee....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...E....Ee....|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to right", 
                "..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to left", 
                "..............|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top right (diagonal)", 
                "..............|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top left (diagonal)", 
                "..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom right (diagonal)", 
                "..............|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom left (diagonal)", 
                "..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),     
            
            
            // POSITIVE CASE : kill enemy
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top and kill enemy", 
                "...N..........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom and kill enemy", 
                "..............|...e..........|...Q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to right and kill enemy", 
                "..............|...eQ.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to left and kill enemy", 
                "..............|..Pe..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top right (diagonal) and kill enemy", 
                "....e.........|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to top left (diagonal) and kill enemy", 
                "..N...........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),   
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom right (diagonal) and kill enemy", 
                "..............|...e..........|....R.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),  
            new IsValidMoveTestSuite(
                "POSITIVE CASE : evolved pawn move to bottom left (diagonal) and kill enemy", 
                "..............|...e..........|..R...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),



            
            // NEGATIVE CASE for movement out of bounds
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top (2 step)", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom (2 step)", 
                "..............|..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to right (2 step)", 
                "..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to left (2 step)", 
                "..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|e.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top right (diagonal, 2 step)", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top left (diagonal, 2 step)", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "e.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom right (diagonal, 2 step)", 
                "..............|..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom left (diagonal)", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..............|..............|e.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),    
            
            // NEGATIVE CASE for killing friends
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top and kill friend", 
                "...n..........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom and kill friend", 
                "..............|...e..........|...q..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to right and kill friend", 
                "..............|...eq.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to left and kill friend", 
                "..............|..PE..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top right (diagonal) and kill friend", 
                "....N.........|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top left (diagonal) and kill friend", 
                "..n...........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom right (diagonal) and kill friend", 
                "..............|...e..........|....r.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom left (diagonal) and kill friend", 
                "..............|...e..........|..r...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ), 

            // NEGATIVE CASE for override wall position
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top and kill wall", 
                "...0..........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom and kill wall", 
                "..............|...e..........|...0..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to right and kill wall", 
                "..............|...E0.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to left and kill wall", 
                "..............|..0E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..E...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top right (diagonal) and kill wall", 
                "....0.........|...E..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "....E.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to top left (diagonal) and kill wall", 
                "..0...........|...e..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom right (diagonal) and kill wall", 
                "..............|...e..........|....0.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|....e.........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),  
            new IsValidMoveTestSuite(
                "NEGATIVE CASE : evolved pawn move to bottom left (diagonal) and kill wall", 
                "..............|...e..........|..0...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|..............|..e...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
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
                String.format("[FAILED TEST] MoveValidationModuleTests.EvolvedPawnIsValidMove, failed on double movement scan check, %s\nExpected: %s\nActual: %s", 
                              test.getName(), 
                              isNotDoubleMovement, 
                              true
                            )                
            );


            
            Boolean result = character.isValidMove((PositionDto)movement.get(GameConstants.KEY_OLD_POSITION), oldStateArgToArray, newStateArgToArray);
            assertEquals(
                result, 
                test.getExpectation(), 
                String.format("[FAILED TEST] MoveValidationModuleTests.EvolvedPawnIsValidMove : %s\nExpected: %s\nActual: %s", 
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
        System.out.println(String.format("[TEST REPORT] MoveValidationModuleTests.EvolvedPawnIsValidMove : %s / %s tests passed", tests.length - countFail, tests.length));          
    }

}
