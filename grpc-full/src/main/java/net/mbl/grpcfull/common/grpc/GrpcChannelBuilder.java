package net.mbl.grpcfull.common.grpc;

import io.grpc.ManagedChannel;
import io.netty.channel.EventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * A gRPC channel builder that authenticates with {@link GrpcServer} at the target during channel
 * building.
 */
public final class GrpcChannelBuilder {
    /**
     * gRPC channel key.
     */
    private GrpcChannelKey mChannelKey;

    private GrpcChannelBuilder(GrpcServerAddress address) {
        mChannelKey = GrpcChannelKey.create();
        // Set default overrides for the channel.
        mChannelKey.setServerAddress(address);
        mChannelKey.setMaxInboundMessageSize(100 * 1024 * 1024);
    }

    /**
     * Create a channel builder for given address using the given configuration.
     *
     * @param address the host address
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public static GrpcChannelBuilder newBuilder(GrpcServerAddress address) {
        return new GrpcChannelBuilder(address);
    }

    /**
     * Sets human readable name for the channel's client.
     *
     * @param clientType client type
     * @return the updated {@link GrpcChannelBuilder} instance
     */
    public GrpcChannelBuilder setClientType(String clientType) {
        mChannelKey.setClientType(clientType);
        return this;
    }

    /**
     * Sets the time to wait after receiving last message before pinging the server.
     *
     * @param keepAliveTime the time to wait after receiving last message before pinging server
     * @param timeUnit      unit of the time
     * @return the updated {@link GrpcChannelBuilder} instance
     */
    public GrpcChannelBuilder setKeepAliveTime(long keepAliveTime, TimeUnit timeUnit) {
        mChannelKey.setKeepAliveTime(keepAliveTime, timeUnit);
        return this;
    }

    /**
     * Sets the maximum time waiting for response after pinging server before closing connection.
     *
     * @param keepAliveTimeout the time to wait after pinging server before closing the connection
     * @param timeUnit         unit of the timeout
     * @return the updated {@link GrpcChannelBuilder} instance
     */
    public GrpcChannelBuilder setKeepAliveTimeout(long keepAliveTimeout, TimeUnit timeUnit) {
        mChannelKey.setKeepAliveTimeout(keepAliveTimeout, timeUnit);
        return this;
    }

    /**
     * Sets the maximum message size allowed for a single gRPC frame.
     *
     * @param maxInboundMessaageSize the maximum inbound message size in bytes
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public GrpcChannelBuilder setMaxInboundMessageSize(int maxInboundMessaageSize) {
        mChannelKey.setMaxInboundMessageSize(maxInboundMessaageSize);
        return this;
    }

    /**
     * Sets the flow control window.
     *
     * @param flowControlWindow the flow control window in bytes
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public GrpcChannelBuilder setFlowControlWindow(int flowControlWindow) {
        mChannelKey.setFlowControlWindow(flowControlWindow);
        return this;
    }

    /**
     * Sets the channel type.
     *
     * @param channelType the channel type
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public GrpcChannelBuilder setChannelType(Class<? extends io.netty.channel.Channel> channelType) {
        mChannelKey.setChannelType(channelType);
        return this;
    }

    /**
     * Sets the event loop group.
     *
     * @param group the event loop group
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public GrpcChannelBuilder setEventLoopGroup(EventLoopGroup group) {
        mChannelKey.setEventLoopGroup(group);
        return this;
    }

    /**
     * Sets the pooling strategy.
     *
     * @param strategy the pooling strategy
     * @return a new instance of {@link GrpcChannelBuilder}
     */
    public GrpcChannelBuilder setPoolingStrategy(GrpcChannelKey.PoolingStrategy strategy) {
        mChannelKey.setPoolingStrategy(strategy);
        return this;
    }

    /**
     * Creates an authenticated channel of type {@link GrpcChannel}.
     *
     * @return the built {@link GrpcChannel}
     */
    public GrpcChannel build() {
        ManagedChannel underlyingChannel =
                GrpcManagedChannelPool.INSTANCE().acquireManagedChannel(mChannelKey,
                        5_000,
                        60_000);
        try {
            // Return a wrapper over original channel.
            return new GrpcChannel(mChannelKey, underlyingChannel, 60_000);
        } catch (Exception e) {
            // Release the managed channel to the pool before throwing.
            GrpcManagedChannelPool.INSTANCE().releaseManagedChannel(mChannelKey, 60_000);
            throw e;
        }
    }
}
