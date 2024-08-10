package com.chesshouzs.server.config.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.chesshouzs.server.service.rpc.RpcMatchService;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Configuration
public class GrpcConfig {

    @Autowired
    public RpcMatchService matchService; 

    private Server server;
    
    @PostConstruct
    public void init() throws IOException, InterruptedException {
        server = ServerBuilder.forPort(9091)
        .addService(matchService)
        .build();

        new Thread(() -> {
            try {
                server.start();
                System.out.println("gRPC server started on port 9091");
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
