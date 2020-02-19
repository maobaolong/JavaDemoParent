package net.mbl.grpcfull.common.grpc;

import net.mbl.grpcfull.common.proto.ServiceVersionClientServiceGrpc;
import net.mbl.grpcfull.common.proto.VersionProto;

import io.grpc.stub.StreamObserver;

public class ServiceVersionClientServiceHandler
        extends ServiceVersionClientServiceGrpc.ServiceVersionClientServiceImplBase {
    @Override
    public void getServiceVersion(VersionProto.GetServiceVersionPRequest request,
            StreamObserver<VersionProto.GetServiceVersionPResponse> responseObserver) {
        long serviceVersion = 10;
        responseObserver
                .onNext(VersionProto.GetServiceVersionPResponse.newBuilder().setVersion(serviceVersion).build());
        responseObserver.onCompleted();
    }
}
