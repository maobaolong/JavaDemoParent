package net.mbl.grpcfull.common.grpc;

import com.google.common.io.Closer;
import io.grpc.ServerInterceptor;
import io.grpc.ServerServiceDefinition;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Provides authenticated gRPC server creation.
 */
public final class GrpcServerBuilder {

    /**
     * Internal netty builder.
     */
    private NettyServerBuilder mNettyServerBuilder;

    /**
     * Used to register closers that needs to be called during server shut-down.
     */
    private Closer mCloser = Closer.create();

    private GrpcServerBuilder(GrpcServerAddress serverAddress) {
        mNettyServerBuilder = NettyServerBuilder.forAddress(serverAddress.getSocketAddress());
    }

    /**
     * Create an new instance of {@link GrpcServerBuilder} with authentication support.
     *
     * @param serverAddress server address
     * @return a new instance of {@link GrpcServerBuilder}
     */
    public static GrpcServerBuilder forAddress(GrpcServerAddress serverAddress) {
        return new GrpcServerBuilder(serverAddress);
    }

    /**
     * Set the executor for this server.
     *
     * @param executor the executor
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder executor(@Nullable Executor executor) {
        mNettyServerBuilder = mNettyServerBuilder.executor(executor);
        return this;
    }

    /**
     * Sets flow control window.
     *
     * @param flowControlWindow the HTTP2 flow control window
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder flowControlWindow(int flowControlWindow) {
        mNettyServerBuilder = mNettyServerBuilder.flowControlWindow(flowControlWindow);
        return this;
    }

    /**
     * Sets the keep alive time.
     *
     * @param keepAliveTime the time to wait after idle before pinging client
     * @param timeUnit      unit of the time
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder keepAliveTime(long keepAliveTime, TimeUnit timeUnit) {
        mNettyServerBuilder = mNettyServerBuilder.keepAliveTime(keepAliveTime, timeUnit);
        return this;
    }

    /**
     * Sets the keep alive timeout.
     *
     * @param keepAliveTimeout time to wait after pinging client before closing the connection
     * @param timeUnit         unit of the timeout
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder keepAliveTimeout(long keepAliveTimeout, TimeUnit timeUnit) {
        mNettyServerBuilder = mNettyServerBuilder.keepAliveTimeout(keepAliveTimeout, timeUnit);
        return this;
    }

    /**
     * Sets the netty channel type.
     *
     * @param channelType the netty channel type for the server
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder channelType(Class<? extends ServerChannel> channelType) {
        mNettyServerBuilder = mNettyServerBuilder.channelType(channelType);
        return this;
    }

    /**
     * Sets a netty channel option.
     *
     * @param <T>    channel option type
     * @param option the option to be set
     * @param value  the new value
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public <T> GrpcServerBuilder withChildOption(ChannelOption<T> option, T value) {
        mNettyServerBuilder = mNettyServerBuilder.withChildOption(option, value);
        return this;
    }

    /**
     * Sets the boss {@link EventLoopGroup}.
     *
     * @param bossGroup the boss event loop group
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder bossEventLoopGroup(EventLoopGroup bossGroup) {
        mNettyServerBuilder = mNettyServerBuilder.bossEventLoopGroup(bossGroup);
        return this;
    }

    /**
     * Sets the worker {@link EventLoopGroup}.
     *
     * @param workerGroup the worker event loop group
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder workerEventLoopGroup(EventLoopGroup workerGroup) {
        mNettyServerBuilder = mNettyServerBuilder.workerEventLoopGroup(workerGroup);
        return this;
    }

    /**
     * Sets the maximum size of inbound messages.
     *
     * @param messageSize maximum size of the message
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder maxInboundMessageSize(int messageSize) {
        mNettyServerBuilder = mNettyServerBuilder.maxInboundMessageSize(messageSize);
        return this;
    }

    /**
     * Add a service to this server.
     *
     * @param serviceDefinition the service definition of new service
     * @return an updated instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder addService(GrpcService serviceDefinition) {
        ServerServiceDefinition service = serviceDefinition.getServiceDefinition();
        mNettyServerBuilder = mNettyServerBuilder.addService(service);
        mCloser.register(serviceDefinition.getCloser());
        return this;
    }

    /**
     * Adds an interceptor for this server.
     *
     * @param interceptor server interceptor
     * @return an updates instance of this {@link GrpcServerBuilder}
     */
    public GrpcServerBuilder intercept(ServerInterceptor interceptor) {
        mNettyServerBuilder = mNettyServerBuilder.intercept(interceptor);
        return this;
    }

    /**
     * Build the server.
     * It attaches required services and interceptors for authentication.
     *
     * @return the built {@link GrpcServer}
     */
    public GrpcServer build() {
        addService(new GrpcService(new ServiceVersionClientServiceHandler())
                .disableAuthentication());
        return new GrpcServer(mNettyServerBuilder.build(), mCloser,
                60_000);
    }
}
