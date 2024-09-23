package com.chesshouzs.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.util.PropertySource.Comparator;
import org.aspectj.weaver.Position;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
                "POSITIVE CASE : king is in check and got attacked horizontally with one queen (single attacker)", 
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
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked horizontally with one queen (single attacker) (2)", 
                "..............|..............|..Q.........k.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.WHITE_COLOR, new PositionDto(2, 2)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,11),
                    new PositionDto(1,12),
                    new PositionDto(1,13),
                    new PositionDto(3,11),
                    new PositionDto(3,12),
                    new PositionDto(3,13)
                )
            ),             
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked vertically with one queen and friend near king (single attacker)", 
                "..............|..............|KR............|..............|..............|q.............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.BLACK_COLOR, new PositionDto(5, 0)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,1),
                    new PositionDto(3,1)
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked vertically with one rook and unthreatening enemy near king (single attacker)", 
                "..............|..............|Kn............|..............|..............|r.............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Rook(GameConstants.BLACK_COLOR, new PositionDto(5, 0)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,1),
                    new PositionDto(2,1),
                    new PositionDto(3,1)
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked vertically with one bishop and threatening rook blocking several squares near king (single attacker)", 
                ".....R........|..............|....k.........|..............|......B.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Bishop(GameConstants.WHITE_COLOR, new PositionDto(4, 6)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,4),
                    new PositionDto(2,3),
                    new PositionDto(3,3),
                    new PositionDto(3,4)
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked vertically with one bishop and threatening rook blocking several squares near king (single attacker) (2)", 
                "..............|.....R........|....k.........|..............|......B.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Bishop(GameConstants.WHITE_COLOR, new PositionDto(4, 6)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,5),
                    new PositionDto(2,3),
                    new PositionDto(3,3),
                    new PositionDto(3,4)
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by knight with threatening evolved pawn several squares nearby", 
                "..............|........N.....|....E.k.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Knight(GameConstants.WHITE_COLOR, new PositionDto(1, 8)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,6),
                    new PositionDto(1,7),
                    new PositionDto(2,7),
                    new PositionDto(3,6)
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn", 
                "..............|...e..........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,1),
                    new PositionDto(1,3),
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)

                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn, but evolved pawn is covered by enemy king", 
                "..............|...ek.........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,1),
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)
                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn, but evolved pawn is covered by enemy pawn", 
                "....p.........|...e..........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,1),
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)
                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn, but evolved pawn is covered by enemy pawn (2)", 
                "..p...........|...e..........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)
                )
            ),       
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn, but evolved pawn is covered by enemy king", 
                "..k...........|...e..........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)
                )
            ),      
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by evolved pawn (2)", 
                "k.............|...e..........|..K...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.BLACK_COLOR, new PositionDto(1, 3)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,3),
                    new PositionDto(2,1),
                    new PositionDto(3,1),
                    new PositionDto(3,2), 
                    new PositionDto(3,3)
                )
            ),
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn", 
                "..............|............PN|.............k|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(1, 12)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,12),
                    new PositionDto(1,13),
                    new PositionDto(2,12),
                    new PositionDto(3,13)
                )
            ),             
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (2)", 
                "..............|.....P........|......k.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(1, 5)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,5),
                    new PositionDto(1,6),
                    new PositionDto(1,7),
                    new PositionDto(2,5),
                    new PositionDto(2,7),
                    new PositionDto(3,5),
                    new PositionDto(3,6),
                    new PositionDto(3,7)                    
                )
            ),  
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (3)", 
                "..............|.......p......|......K.......|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.BLACK_COLOR, new PositionDto(1, 7)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,5),
                    new PositionDto(1,6),
                    new PositionDto(1,7),
                    new PositionDto(2,5),
                    new PositionDto(2,7),
                    new PositionDto(3,5),
                    new PositionDto(3,6),
                    new PositionDto(3,7)                    
                )
            ),  
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (4)", 
                ".P...........P|k.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(0, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,0),
                    new PositionDto(0,1),
                    new PositionDto(1,1),
                    new PositionDto(2,0),
                    new PositionDto(2,1)
                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (5)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|............P.|.............k|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(12, 12)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(12,12),
                    new PositionDto(12,13),
                    new PositionDto(13,12)
                )
            ),
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (6)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.P............|k.............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(12, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(12,0),
                    new PositionDto(12,1),
                    new PositionDto(13,1)
                )
            ),                                  
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (7)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..P...........|...k..........|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(12, 2)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(12,2),
                    new PositionDto(12,3),
                    new PositionDto(12,4),
                    new PositionDto(13,2),
                    new PositionDto(13,4)
                )
            ),  
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by pawn (8)", 
                "..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|.....P........|....P.........|...k..........|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Pawn(GameConstants.WHITE_COLOR, new PositionDto(12, 4)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(12,2),
                    new PositionDto(12,3),
                    new PositionDto(13,2),
                    new PositionDto(13,4)
                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by rook with enemy knight inline", 
                ".r............|.K............|.n............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.WHITE_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Rook(GameConstants.BLACK_COLOR, new PositionDto(0, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,1),
                    new PositionDto(1,0),
                    new PositionDto(1,2),
                    new PositionDto(2,0), 
                    new PositionDto(2,2)
                )
            ),       
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by queen with enemy bishop inline", 
                ".QkB..........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.WHITE_COLOR, new PositionDto(0, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,1),
                    new PositionDto(1,3)
                )
            ),  
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by queen with wall around", 
                ".Q.........k0.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.WHITE_COLOR, new PositionDto(0, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,10),
                    new PositionDto(1,11),
                    new PositionDto(1,12)
                )
            ),   
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by rook vertically in face to face", 
                "R...........0.|k.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Rook(GameConstants.WHITE_COLOR, new PositionDto(0, 0)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,0),
                    new PositionDto(1,1),
                    new PositionDto(2,1)
                )
            ),      
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got attacked by bishop in face to face", 
                "B...........0.|.k............|..P...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Bishop(GameConstants.WHITE_COLOR, new PositionDto(0, 0)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,0),
                    new PositionDto(0,1),
                    new PositionDto(0,2),
                    new PositionDto(1,0),
                    new PositionDto(2,0),
                    new PositionDto(2,1),
                    new PositionDto(1,2)
                )
            ),      
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check and got two rooks and has no movement left", 
                "R............k|R.............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Rook(GameConstants.WHITE_COLOR, new PositionDto(0, 0)));
                }},                 
                Helper.generateHashBasedTable()
            ),
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check, got attacked by one evolved pawn with enemy knight around", 
                "..............|............kE|............N.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.WHITE_COLOR, new PositionDto(1, 13)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(1,11),                    
                    new PositionDto(1,13),                     
                    new PositionDto(2,11)                     
                )
            ), 
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check, got attacked by one evolved pawn with teammate knight around", 
                "..............|............kE|............n.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new EvolvedPawn(GameConstants.WHITE_COLOR, new PositionDto(1, 13)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,11),                    
                    new PositionDto(1,11),                    
                    new PositionDto(1,13),                     
                    new PositionDto(2,11)                     
                )
            ),                                       
                                                                                                          
            // POSITIVE CASE : discovered check (double attacker)                                                           
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check, got attacked by one rook and one bishop", 
                "..............|.R.k..........|..B...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Bishop(GameConstants.WHITE_COLOR, new PositionDto(2, 2)));
                    add(new Rook(GameConstants.WHITE_COLOR, new PositionDto(1, 1)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,2),                    
                    new PositionDto(0,3),                 
                    new PositionDto(2,2),           
                    new PositionDto(2,3),                    
                    new PositionDto(2,4)                   
                )
            ),        
            new InCheckTestSuite(
                "POSITIVE CASE : king is in check, got attacked by one queen and one evolved pawn", 
                "..............|.Q............|..E...........|.k............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                true, 
                new ArrayList<Character>(){{
                    add(new Queen(GameConstants.WHITE_COLOR, new PositionDto(1, 1)));
                    add(new EvolvedPawn(GameConstants.WHITE_COLOR, new PositionDto(2, 2)));
                }},                 
                Helper.generateHashBasedTable(
                    new PositionDto(3,0),                    
                    new PositionDto(4,0),                 
                    new PositionDto(4,2)          
                )
            ),                    

            // NEGATIVE CASE : inline with rook / queen (teammate)
            new InCheckTestSuite(
                "NEGATIVE CASE : king is not in check (1)", 
                "............P.|.q..........k.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                GameConstants.BLACK_COLOR,
                false, 
                new ArrayList<Character>(){{}},                 
                Helper.generateHashBasedTable(
                    new PositionDto(0,11),  
                    new PositionDto(0,12),                    
                    new PositionDto(0,13),                    
                    new PositionDto(2,11),                    
                    new PositionDto(2,12),                    
                    new PositionDto(2,13)                 
                )
            ),                    

        };        

        int countFail = 0;
        for (InCheckTestSuite test : tests){
            System.out.println("\n" + test.getName());
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

                assertInstanceOf(
                    test.getExpectedAttackers().get(i).getClass(), 
                    resultAttackers.get(i), 
                    String.format(
                        "[FAILED TEST] InCheckModuleTests.InCheckStatusTest, attacker type does not match : %s\nExpected: %s\nActual: %s", 
                        test.getName(), 
                        test.getExpectedAttackers().get(i).getClass(), 
                        resultAttackers.get(i).getClass()
                    )
                );

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
