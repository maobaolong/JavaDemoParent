package net.mbl.grpcfull.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ClientMasterRpcServer {
    /**
     * grpc port
     */
    public static final int DEFAULT_PORT = 8888;
    /**
     * grpc server
     */
    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        // for quick try purpose
        final ClientMasterRpcServer server = new ClientMasterRpcServer();
        server.start(DEFAULT_PORT);
        server.blockUntilShutdown();
    }

    public int start(int port) throws IOException {
        // start rpc service
        server = ServerBuilder.forPort(port).addService(new ClientMasterServiceImpl())
                .build().start();
        log.info("service start with port {} ...", server.getPort());
        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.error("*** shutting down gRPC server since JVM is shutting down");
                ClientMasterRpcServer.this.stop();
                log.error("*** server shut down");
            }
        });
        return server.getPort();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}
