package com.chesshouzs.server.service.rpc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chesshouzs.server.service.lib.usecase.Game;
import com.chesshouzs.server.service.rpc.module.RpcGameModule;
import com.chesshouzs.server.service.rpc.pb.MatchServiceGrpc;
import com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq;
import com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class RpcMatchService extends MatchServiceGrpc.MatchServiceImplBase {

    @Autowired 
    private RpcGameModule rpcGameModule;

    @Override
    public void validateMove(ValidateMoveReq request, StreamObserver<ValidateMoveResp> responseObserver) {

        // get old state (from redis)
        /*  compare old state with new state (from request body)
            - if player color is black then flip the board
            - check the difference 
            - if there is more than two piece difference, automatically set invalid (false)
            - get the moving piece
            - check origin position and move destination with these validations in order + check if king is in check
                - if king not in check :
                    - if movement is not from king : 
                        - if piece movement causes king to be in check then invalid
                        - valid move for each piece
                    - else (king is moving)
                        - move must not cause king to be in check 
                        - valid move for king
                - else (king in check):
                    - validate from old state 
                        - if king has no valid moves: 
                            - check if another piece can kill the attacker OR block the attack
                            - if no savior, then triggers checkmate
                        - else, new move must cause king to be safe 
                            - either (kill the attacker and still be save) OR king move to save position
                            - if movement is from king : 
                                - new move must keep king safety (even after killing enemy attacker)
                            - else
                                - new move must kill the attacker OR block the attack
        */

        System.out.println("OLD STATE ");
        System.out.println(request.getOldState()); 
        System.out.println(request.getOldState().length()); 
        System.out.println("NEW STATE ");
        System.out.println(request.getNewState());
        System.out.println(request.getNewState().length()); 
        Boolean isValid = rpcGameModule.validateMovement(request.getOldState(),request.getNewState());
        System.out.println(isValid);

        // setup response
        ValidateMoveResp response = ValidateMoveResp.newBuilder()
                .setValid(isValid)
                .build();

        // response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

