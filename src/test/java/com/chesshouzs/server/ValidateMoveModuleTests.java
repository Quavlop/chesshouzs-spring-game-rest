package com.chesshouzs.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chesshouzs.server.service.rpc.module.RpcGameModule;
import com.chesshouzs.server.utils.ValidateMoveTestSuite;

public class ValidateMoveModuleTests {

    @Autowired
    private RpcGameModule rpcGameModule;
    
    @Test
    void ValidateMovementTest() throws Exception {

        rpcGameModule = new RpcGameModule();

        ValidateMoveTestSuite[] tests = {
            // POSITIVE CASE
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (1)", 
                "........k.....|..............|.B............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........k.....|..............|.q............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (2)", 
                "........k....Q|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........k...rQ|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3)", 
                "........k..r.Q|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........k...rQ|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                true
            ),           
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-1)", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..........n...|pppppppppppppp|ppnbpprrppb.pp|rnbnbnqknbnbnr", 
                true
            ),   
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-2)", 
                "RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr|", 
                "RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..........n...|pppppppppppppp|ppnbpprrppb.pp|rnbnbnqknbnbnr|", 
                true
            ),   
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-3)", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr|", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..........n...|pppppppppppppp|ppnbpprrppb.pp|rnbnbnqknbnbnr|", 
                true
            ),   
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-4)", 
                "RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr", 
                "RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..........n...|pppppppppppppp|ppnbpprrppb.pp|rnbnbnqknbnbnr", 
                true
            ),   
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-5)", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..........n...|pppppppppppppp|ppnbpprrppb.pp|rnbnbnqknbnbnr", 
                true
            ),        
            new ValidateMoveTestSuite(
                "POSITIVE CASE : valid move (3-6)", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|..............|..............|pppppppppppppp|ppnbpprrppbnpp|rnbnbnqknbnbnr", 
                "|RNBNBNQKNBNBNR|PPNBPPRRPPBNPP|PPPPPP.PPPPPPP|..............|......P.......|..............|..............|..............|..............|...........p..|..............|ppppppppppp.pp|ppnbpprrppbnpp|rnbnbnqknbnbnr", 
                true
            ),                                                     
            
            // NEGATIVE CASE
            new ValidateMoveTestSuite(
                "NEGATIVE CASE : invalid move (1)", 
                "........k.....|..............|.B............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........k.....|..............|..q...........|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new ValidateMoveTestSuite(
                "NEGATIVE CASE : invalid move (2)", 
                "........k....Q|............r.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "........k....Q|...........r..|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),   
            new ValidateMoveTestSuite(
                "NEGATIVE CASE : invalid move (3)", 
                "k.............|.......b....R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                "..............|.k..........R.|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|..............|", 
                false
            ),                                 
        };

        for (ValidateMoveTestSuite test : tests){
            System.out.println(test.getName());
            Boolean result = rpcGameModule.validateMovement(test.getOldStateArg(), test.getNewStateArg());
            assertEquals(
                test.getExpectation(), 
                result,
                String.format(
                    "[FAILED TEST] ValidateMoveModuleTests.ValidateMovementTest, failed scenario validation : %s\nExpected: %s\nActual: %s", 
                    test.getName(), 
                    test.getExpectation(), 
                    result
                )                
            );
        }

    }

}

