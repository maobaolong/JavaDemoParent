package net.mbl.grpcfull.example;

import net.mbl.grpcfull.common.ProcessUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MasterRpcTest {
    @Test
    public void testMasterRpc() throws IOException {
        MasterProcess process = new MasterProcess();
        new Thread(() -> ProcessUtils.run(process)).start();

        InetSocketAddress address = process.getRpcAddress();

        MasterRpcClient client = new MasterRpcClient(address.getHostName(), address.getPort(), "jobId1");
        client.end();
    }
}
