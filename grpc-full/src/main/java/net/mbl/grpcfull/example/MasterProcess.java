package net.mbl.grpcfull.example;

import net.mbl.grpcfull.common.grpc.GrpcServer;
import net.mbl.grpcfull.common.grpc.GrpcServerAddress;
import net.mbl.grpcfull.common.grpc.GrpcServerBuilder;
import net.mbl.grpcfull.common.grpc.GrpcService;
import net.mbl.grpcfull.common.Process;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MasterProcess implements Process {

    /**
     * Rpc server bind address.
     **/
    final InetSocketAddress mRpcBindAddress;
    /**
     * The RPC server.
     */
    protected GrpcServer mGrpcServer;

    private ExecutorService mRPCExecutor = null;

    public MasterProcess() {
        // TODO(shenlong): remove hardcode
        mRpcBindAddress = new InetSocketAddress("localhost", 8888);
    }

    public InetSocketAddress getRpcAddress() {
        return mRpcBindAddress;
    }

    protected void startServing() {
        startServing("", "");
    }

    /**
     * Starts serving, start RPC Server.
     * ll
     *
     * @param startMessage empty string or the message
     * @param stopMessage  empty string or the message
     */
    protected void startServing(String startMessage, String stopMessage) {
        log.info(
                "started{}. bindAddress={}",
                startMessage, mRpcBindAddress);
        startServingRPCServer();
        log.info("ended{}", stopMessage);
    }

    /**
     * Starts the gRPC server.
     */
    protected void startServingRPCServer() {
        try {
            log.info("Starting gRPC server on address {}", mRpcBindAddress);
            GrpcServerBuilder serverBuilder = GrpcServerBuilder.forAddress(
                    GrpcServerAddress.create(mRpcBindAddress.getHostName(), mRpcBindAddress));

            int nThreads = 50;
            mRPCExecutor =
                    new ThreadPoolExecutor(nThreads, nThreads,
                            0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("grpc-rpc-%d").build());

            serverBuilder.executor(mRPCExecutor);

            serverBuilder.addService(new GrpcService(new ClientMasterServiceImpl()));

            mGrpcServer = serverBuilder.build().start();
            log.info("Started gRPC server on address {}", mRpcBindAddress);

            // Wait until the server is shut down.
            mGrpcServer.awaitTermination();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stops serving, trying stop RPC server.
     */
    protected void stopServing() throws Exception {
        if (isServing()) {
            if (!mGrpcServer.shutdown()) {
                log.warn("RPC server shutdown timed out.");
            }
        }
        if (mRPCExecutor != null) {
            mRPCExecutor.shutdownNow();
            try {
                mRPCExecutor.awaitTermination(
                        60_000,
                        TimeUnit.MILLISECONDS);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public String toString() {
        return "Process @" + mRpcBindAddress;
    }

    public boolean isServing() {
        return mGrpcServer != null && mGrpcServer.isServing();
    }

    @Override
    public void start() throws Exception {
        startServing();
    }

    @Override
    public void stop() throws Exception {
        if (isServing()) {
            stopServing();
        }
    }

    @Override
    public boolean waitForReady(int timeoutMs) {
        return false;
    }
}
