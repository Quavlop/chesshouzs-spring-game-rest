package com.chesshouzs.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Map;

import org.aspectj.weaver.Position;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.game.King;
import com.chesshouzs.server.service.lib.game.Queen;
import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.service.lib.usecase.CharacterUsecase;
import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.Helper;
import com.chesshouzs.server.utils.InCheckTestSuite;
import com.datastax.oss.driver.shaded.guava.common.collect.HashBasedTable;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;

@SpringBootTest
public class InCheckModuleTests {
 
    @Test
    void InCheckStatusTest() throws Exception {
        // state here is new state
        InCheckTestSuite[] tests = {

            // POSITIVE CASE : regular single attacks
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked with one rook (single attacker)", 
                "..............|..............|..Q..........k|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.WHITE_COLOR, new PositionDto(2, 2)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,12),
                    new PositionDto(1,13),
                    new PositionDto(3,12),
                    new PositionDto(3,13)
                )
            )
            

        };        

        int countFail = 0;
        for (InCheckTestSuite test : tests){
            char[][] stateToArr = GameHelper.convertNotationToArray(test.getState());

            King king = CharacterUsecase.findKing(stateToArr, test.getPlayerColor());
            assertNotNull(
                king, 
                String.format(
                    "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, failed to locate king : %s", 
                    test.getName() 
                )
            );

            Map<String, Object> result = king.inCheckStatus(stateToArr);

            ArrayList<Character> resultAttackers = (ArrayList<Character>)result.get(GameConstants.KEY_ATTACKERS);
            assertEquals(
                test.getExpectedAttackers().size(), 
                resultAttackers.size(), 
                String.format(
                    "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, number of attackers should match : %s\nExpected: %s\nActual: %s", 
                    test.getName(), 
                    test.getExpectedAttackers().size(), 
                    resultAttackers.size()
                )
            );
            for (int i = 0; i < test.getExpectedAttackers().size(); i++) {
                Boolean isComparisonResultEqual = Helper.compareJson(test.getExpectedAttackers().get(i), resultAttackers.get(i));
                if (!isComparisonResultEqual){
                    countFail += 1;
                }
                assertEquals(
                    isComparisonResultEqual,
                    true,
                    String.format(
                        "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, attacker data does not match : %s\nExpected: %s\nActual: %s", 
                        test.getName(), 
                        Helper.convertObjectToJson(test.getExpectedAttackers().get(i)),
                        Helper.convertObjectToJson(resultAttackers.get(i))
                    )
                );
            }    

            Boolean resultIsInCheck = (Boolean)result.get(GameConstants.KEY_IS_IN_CHECK); 
            assertEquals(
                test.getExpectedIsInCheck(), 
                resultIsInCheck, 
                String.format(
                    "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, king check status does not match : %s\nExpected: %s\nActual: %s", 
                    test.getName(),  
                    test.getExpectedIsInCheck(), 
                    resultIsInCheck
                )
            );


            Table<Integer, Integer, Boolean> resultValidMoves = (Table<Integer, Integer, Boolean>)result.get(GameConstants.KEY_VALID_MOVES);
            assertEquals(
                test.getExpectedValidKingMoves().cellSet(), 
                resultValidMoves.cellSet(), 
                String.format(
                    "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, valid king moves should match : %s\nExpected: %s\nActual: %s", 
                    test.getName(), 
                    Helper.convertObjectToJson(test.getExpectedValidKingMoves().cellSet()),
                    Helper.convertObjectToJson(resultValidMoves.cellSet())
                )
            );
        }

        if (countFail <= 0){
            System.out.println("==== PASSED ALL TEST CASE ====");
        } else {
            System.out.println("==== FAILED ====");
        }
        System.out.println(String.format("[TEST REPORT] InCheckModuleTests.InCheckStatusTest : %s / %s tests passed", tests.length - countFail, tests.length));        
    }

}
