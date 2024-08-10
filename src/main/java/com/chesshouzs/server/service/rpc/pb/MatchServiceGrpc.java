package com.chesshouzs.server.service.rpc.pb;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.50.0)",
    comments = "Source: match_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MatchServiceGrpc {

  private MatchServiceGrpc() {}

  public static final String SERVICE_NAME = "pb.MatchService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq,
      com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> getValidateMoveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateMove",
      requestType = com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq.class,
      responseType = com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq,
      com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> getValidateMoveMethod() {
    io.grpc.MethodDescriptor<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq, com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> getValidateMoveMethod;
    if ((getValidateMoveMethod = MatchServiceGrpc.getValidateMoveMethod) == null) {
      synchronized (MatchServiceGrpc.class) {
        if ((getValidateMoveMethod = MatchServiceGrpc.getValidateMoveMethod) == null) {
          MatchServiceGrpc.getValidateMoveMethod = getValidateMoveMethod =
              io.grpc.MethodDescriptor.<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq, com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateMove"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp.getDefaultInstance()))
              .setSchemaDescriptor(new MatchServiceMethodDescriptorSupplier("ValidateMove"))
              .build();
        }
      }
    }
    return getValidateMoveMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MatchServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchServiceStub>() {
        @java.lang.Override
        public MatchServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchServiceStub(channel, callOptions);
        }
      };
    return MatchServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MatchServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchServiceBlockingStub>() {
        @java.lang.Override
        public MatchServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchServiceBlockingStub(channel, callOptions);
        }
      };
    return MatchServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MatchServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatchServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MatchServiceFutureStub>() {
        @java.lang.Override
        public MatchServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MatchServiceFutureStub(channel, callOptions);
        }
      };
    return MatchServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class MatchServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void validateMove(com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq request,
        io.grpc.stub.StreamObserver<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateMoveMethod(), responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getValidateMoveMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq,
                com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp>(
                  this, METHODID_VALIDATE_MOVE)))
          .build();
    }
  }

  /**
   */
  public static final class MatchServiceStub extends io.grpc.stub.AbstractAsyncStub<MatchServiceStub> {
    private MatchServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateMove(com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq request,
        io.grpc.stub.StreamObserver<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateMoveMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MatchServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<MatchServiceBlockingStub> {
    private MatchServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp validateMove(com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateMoveMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MatchServiceFutureStub extends io.grpc.stub.AbstractFutureStub<MatchServiceFutureStub> {
    private MatchServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatchServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatchServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp> validateMove(
        com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateMoveMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_MOVE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MatchServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MatchServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VALIDATE_MOVE:
          serviceImpl.validateMove((com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveReq) request,
              (io.grpc.stub.StreamObserver<com.chesshouzs.server.service.rpc.pb.MatchServiceModel.ValidateMoveResp>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MatchServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MatchServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.chesshouzs.server.service.rpc.pb.MatchServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MatchService");
    }
  }

  private static final class MatchServiceFileDescriptorSupplier
      extends MatchServiceBaseDescriptorSupplier {
    MatchServiceFileDescriptorSupplier() {}
  }

  private static final class MatchServiceMethodDescriptorSupplier
      extends MatchServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MatchServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MatchServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MatchServiceFileDescriptorSupplier())
              .addMethod(getValidateMoveMethod())
              .build();
        }
      }
    }
    return result;
  }
}
