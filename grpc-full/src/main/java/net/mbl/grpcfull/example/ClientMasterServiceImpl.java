package net.mbl.grpcfull.example;

import io.grpc.stub.StreamObserver;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.GetExtraConfigRequestProto;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.GetExtraConfigResponseProto;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.EndRequestProto;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.MasterResponseProto;
import net.mbl.grpcfull.common.proto.ClientMasterServiceProtocolGrpc.ClientMasterServiceProtocolImplBase;

import java.util.HashMap;
import java.util.Map;

public class ClientMasterServiceImpl extends ClientMasterServiceProtocolImplBase {
    @Override
    public void getExtraConfig(GetExtraConfigRequestProto request,
            StreamObserver<GetExtraConfigResponseProto> responseObserver) {
        Map<String, String> configurations = new HashMap<>();
        GetExtraConfigResponseProto response = GetExtraConfigResponseProto.newBuilder()
                .setSuccess(true)
                .putAllConfigurations(configurations)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void end(EndRequestProto request,
            StreamObserver<MasterResponseProto> responseObserver) {
        MasterResponseProto response = MasterResponseProto.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
