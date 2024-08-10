package com.chesshouzs.server.service.rpc;

import com.chesshouzs.server.service.rpc.pb.MatchServiceGrpc;
import com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq;
import com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class RpcMatchService extends MatchServiceGrpc.MatchServiceImplBase {

    @Override
    public void validateMove(ValidateMoveReq request, StreamObserver<ValidateMoveResp> responseObserver) {
        boolean isValid = true;

        // setup response
        ValidateMoveResp response = ValidateMoveResp.newBuilder()
                .setValid(isValid)
                .build();

        // response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

