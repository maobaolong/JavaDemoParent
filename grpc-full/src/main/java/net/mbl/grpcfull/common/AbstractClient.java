package net.mbl.grpcfull.common;

import net.mbl.grpcfull.common.excpetion.ExceptionMessage;
import net.mbl.grpcfull.common.excpetion.PreconditionMessage;
import net.mbl.grpcfull.common.grpc.GrpcChannel;
import net.mbl.grpcfull.common.grpc.GrpcChannelBuilder;
import net.mbl.grpcfull.common.grpc.GrpcServerAddress;
import net.mbl.grpcfull.common.proto.ServiceVersionClientServiceGrpc;
import net.mbl.grpcfull.common.proto.VersionProto;
import net.mbl.grpcfull.common.retry.RetryPolicy;
import net.mbl.grpcfull.common.retry.RetryUtils;

import com.google.common.base.Preconditions;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.UnresolvedAddressException;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * The base class for clients.
 */
@ThreadSafe
public abstract class AbstractClient implements Client {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractClient.class);

    private final Supplier<RetryPolicy> mRetryPolicySupplier;

    protected InetSocketAddress mAddress;

    /**
     * Underlying channel to the target service.
     */
    protected GrpcChannel mChannel;

    /**
     * Used to query service version for the remote service type.
     */
    protected ServiceVersionClientServiceGrpc.ServiceVersionClientServiceBlockingStub mVersionService;

    /**
     * Is true if this client is currently connected.
     */
    protected boolean mConnected = false;

    /**
     * Is true if this client was closed by the user. No further actions are possible after the client
     * is closed.
     */
    protected volatile boolean mClosed = false;

    /**
     * Stores the service version; used for detecting incompatible client-server pairs.
     */
    protected long mServiceVersion;

    /**
     * Creates a new client base.
     *
     * @param address the address
     */
    public AbstractClient(InetSocketAddress address) {
        this(address,
            () -> RetryUtils.defaultClientRetry(Duration.ofMillis(2 * 60_000),
                        Duration.ofMillis(50), Duration.ofMillis(3_000)));
    }

    /**
     * Creates a new client base.
     *
     * @param address             the address
     * @param retryPolicySupplier factory for retry policies to be used when performing RPCs
     */
    public AbstractClient(InetSocketAddress address,
            Supplier<RetryPolicy> retryPolicySupplier) {
        mAddress = address;
        mRetryPolicySupplier = retryPolicySupplier;
        mServiceVersion = Constants.UNKNOWN_SERVICE_VERSION;
    }

    protected long getRemoteServiceVersion() throws IOException {
        return retryRPC(new RpcCallable<Long>() {
            @Override
            public Long call() {
                return mVersionService.getServiceVersion(
                        VersionProto.GetServiceVersionPRequest.newBuilder().build())
                        .getVersion();
            }
        });
    }

    /**
     * @return a string representing the specific service
     */
    protected abstract String getServiceName();

    /**
     * @return the client service version
     */
    protected abstract long getServiceVersion();

    /**
     * Checks that the service version is compatible with the client.
     *
     * @param clientVersion the client version
     */
    protected void checkVersion(long clientVersion) throws IOException {
        if (mServiceVersion == Constants.UNKNOWN_SERVICE_VERSION) {
            mServiceVersion = getRemoteServiceVersion();
            if (mServiceVersion != clientVersion) {
                throw new IOException(ExceptionMessage.INCOMPATIBLE_VERSION.getMessage(getServiceName(),
                        clientVersion, mServiceVersion));
            }
        }
    }

    /**
     * This method is called after the connection is made to the remote. Implementations should create
     * internal state to finish the connection process.
     */
    protected void afterConnect() throws IOException {
        // Empty implementation.
    }

    /**
     * This method is called before the connection is connected. Implementations should add any
     * additional operations before the connection is connected.
     * loading the cluster defaults
     */
    protected void beforeConnect()
            throws IOException {
        // Bootstrap once for clients
        // TODO(shenlong): load the conf from config service if is not connected.
    }

    /**
     * This method is called after the connection is disconnected. Implementations should clean up any
     * additional state created for the connection.
     */
    protected void afterDisconnect() {
        // Empty implementation.
    }

    /**
     * This method is called before the connection is disconnected. Implementations should add any
     * additional operations before the connection is disconnected.
     */
    protected void beforeDisconnect() {
        // Empty implementation.
    }

    /**
     * Connects with the remote.
     */
    @Override
    public synchronized void connect() throws IOException {
        if (mConnected) {
            return;
        }
        disconnect();
        Preconditions.checkState(!mClosed, "Client is closed, will not try to connect.");

        IOException lastConnectFailure = null;
        RetryPolicy retryPolicy = mRetryPolicySupplier.get();

        while (retryPolicy.attempt()) {
            if (mClosed) {
                throw new IOException("Failed to connect: client has been closed");
            }
            mAddress = getAddress();
            try {
                beforeConnect();
                LOG.debug("client (version {}) is trying to connect with {} @ {}",
                        "dummyBuild info", getServiceName(), mAddress);
                mChannel = GrpcChannelBuilder
                        .newBuilder(GrpcServerAddress.create(mAddress))
                        .setClientType(getServiceName())
                        .build();
                // Create stub for version service on host
                mVersionService = ServiceVersionClientServiceGrpc.newBlockingStub(mChannel);
                mConnected = true;
                afterConnect();
                checkVersion(getServiceVersion());
                LOG.debug("client (version {}) is connected with {} @ {}",
                        "dummy build info",
                        getServiceName(), mAddress);
                return;
            } catch (IOException e) {
                LOG.debug("Failed to connect ({}) with {} @ {}: {}", retryPolicy.getAttemptCount(),
                        getServiceName(), mAddress, e.getMessage());
                lastConnectFailure = e;
            }
        }
        // Reaching here indicates that we did not successfully connect.

        if (mChannel != null) {
            mChannel.shutdown();
        }

        if (mAddress == null) {
            throw new IOException(
                    String.format("Failed to determine address for %s after %s attempts", getServiceName(),
                            retryPolicy.getAttemptCount()));
        }

        throw new IOException(String.format("Failed to connect to %s @ %s after %s attempts",
                getServiceName(), mAddress, retryPolicy.getAttemptCount()), lastConnectFailure);
    }

    /**
     * Closes the connection with the remote and does the necessary cleanup. It should be used
     * if the client has not connected with the remote for a while, for example.
     */
    @Override
    public synchronized void disconnect() {
        if (mConnected) {
            Preconditions.checkNotNull(mChannel, PreconditionMessage.CHANNEL_NULL_WHEN_CONNECTED);
            LOG.debug("Disconnecting from the {} @ {}", getServiceName(), mAddress);
            beforeDisconnect();
            mChannel.shutdown();
            mConnected = false;
            afterDisconnect();
        }
    }

    /**
     * @return true if this client is connected to the remote
     */
    @Override
    public synchronized boolean isConnected() {
        return mConnected;
    }

    /**
     * Closes the connection with the remote permanently. This instance should be not be reused after
     * closing.
     */
    @Override
    public synchronized void close() {
        disconnect();
        mClosed = true;
    }

    @Override
    public synchronized InetSocketAddress getAddress() throws IOException {
        return mAddress;
    }

    /**
     * The RPC to be executed in {@link #retryRPC(RpcCallable)}.
     *
     * @param <V> the return value of {@link #call()}
     */
    protected interface RpcCallable<V> {
        /**
         * The task where RPC happens.
         *
         * @return RPC result
         * @throws StatusRuntimeException when any exception defined in gRPC happens
         */
        V call() throws StatusRuntimeException;
    }

    /**
     * Tries to execute an RPC defined as a {@link RpcCallable}.
     *
     * @param rpc the RPC call to be executed
     * @param <V> type of return value of the RPC call
     * @return the return value of the RPC call
     */
    protected synchronized <V> V retryRPC(RpcCallable<V> rpc) throws IOException {
        return retryRPCInternal(rpc, () -> null);
    }

    /**
     * Tries to execute an RPC defined as a {@link RpcCallable}. Metrics will be recorded based on
     * the provided rpc name.
     *
     * @param rpc     the RPC call to be executed
     * @param rpcName the human readable name of the RPC call
     * @param <V>     type of return value of the RPC call
     * @return the return value of the RPC call
     */
    protected synchronized <V> V retryRPC(RpcCallable<V> rpc, String rpcName)
            throws IOException {
        try {
            return retryRPCInternal(rpc, null);
        } catch (Exception e) {
            throw e;
        }
    }

    private synchronized <V> V retryRPCInternal(RpcCallable<V> rpc, Supplier<Void> onRetry)
            throws IOException {
        RetryPolicy retryPolicy = mRetryPolicySupplier.get();
        Exception ex = null;
        while (retryPolicy.attempt()) {
            if (mClosed) {
                throw new IOException("Client is closed");
            }
            connect();
            try {
                return rpc.call();
            } catch (StatusRuntimeException e) {
                IOException se = new IOException("rpc error", e);
                if (e.getStatus().getCode() == Status.Code.UNAVAILABLE
                        || e.getStatus().getCode() == Status.Code.CANCELLED
                        || e.getStatus().getCode() == Status.Code.UNAUTHENTICATED
                        || e.getCause() instanceof UnresolvedAddressException) {
                    ex = se;
                } else {
                    throw se;
                }
            }
            LOG.debug("Rpc failed ({}): {}", retryPolicy.getAttemptCount(), ex.toString());
            if (onRetry != null) {
                onRetry.get();
            }
            disconnect();
        }
        throw new IOException("Failed after " + retryPolicy.getAttemptCount()
                + " attempts: " + ex.toString(), ex);
    }

    @Override
    public boolean isClosed() {
        return mClosed;
    }
}
