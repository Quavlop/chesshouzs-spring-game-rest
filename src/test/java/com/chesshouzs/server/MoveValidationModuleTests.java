package com.chesshouzs.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.Helper;
import com.chesshouzs.server.utils.DoubleMovementScanResultTestSuite;

@SpringBootTest
public class MoveValidationModuleTests {

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
                    put(GameConstants.KEY_NEW_POSITION, new PositionDto(0, 1));
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
}
