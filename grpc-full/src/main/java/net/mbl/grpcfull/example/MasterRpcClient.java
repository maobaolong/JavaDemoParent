package net.mbl.grpcfull.example;

import net.mbl.grpcfull.common.AbstractMasterClient;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.EndRequestProto;
import net.mbl.grpcfull.common.proto.ClientMasterProtocolProtos.GetExtraConfigRequestProto;
import net.mbl.grpcfull.common.proto.ClientMasterServiceProtocolGrpc;
import net.mbl.grpcfull.common.proto.ClientMasterServiceProtocolGrpc.ClientMasterServiceProtocolBlockingStub;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

public class MasterRpcClient extends AbstractMasterClient {
    private final String id;
    private ClientMasterServiceProtocolBlockingStub blockingStub = null;

    public MasterRpcClient(String host, int port, String id) {
        super(new InetSocketAddress(host, port));
        this.id = id;
    }

    public Map<String, String> getExtraConfig() throws IOException {
        return retryRPC(() -> blockingStub.getExtraConfig(
                GetExtraConfigRequestProto.newBuilder().setId(id).build())
                .getConfigurationsMap());
    }

    public boolean end() throws IOException {
        return retryRPC(() -> blockingStub.end(
                EndRequestProto.newBuilder().setId(id).build())
                .getSuccess());
    }

    @Override
    protected String getServiceName() {
        return "master-rpc-server";
    }

    @Override
    protected long getServiceVersion() {
        return 10;
    }

    @Override
    protected synchronized void afterConnect() {
        blockingStub = ClientMasterServiceProtocolGrpc.newBlockingStub(mChannel);
    }
}
