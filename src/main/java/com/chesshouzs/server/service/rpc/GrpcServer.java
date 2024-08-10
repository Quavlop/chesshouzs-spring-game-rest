package com.chesshouzs.server.service.rpc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.chesshouzs.server.config.app.GrpcConfig;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Configuration
public class GrpcServer {

    private final GrpcConfig grpcConfig;

    public GrpcServer(GrpcConfig grpcConfig){
        this.grpcConfig = grpcConfig;
    }

    @Autowired
    public RpcMatchService matchService; 
    
    private Server server;
    
    @PostConstruct
    public void init() throws IOException, InterruptedException {
        server = ServerBuilder.forPort(this.grpcConfig.getPort())
        .addService(matchService)
        .build();

        new Thread(() -> {
            try {
                server.start();
                System.out.println("gRPC server started on port " + this.grpcConfig.getPort());
                server.awaitTermination();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

}
