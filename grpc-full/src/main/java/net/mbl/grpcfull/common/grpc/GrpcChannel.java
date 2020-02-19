package net.mbl.grpcfull.common.grpc;

import com.google.common.base.MoreObjects;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

import java.util.function.Supplier;

/**
 * An authenticated gRPC channel. This channel can communicate with servers of type
 * {@link GrpcServer}.
 */
public final class GrpcChannel extends Channel {
    private final GrpcChannelKey mChannelKey;
    private Supplier<Boolean> mChannelHealthState;
    private Channel mChannel;
    private boolean mChannelReleased = false;
    private boolean mChannelHealthy = true;
    private final long mShutdownTimeoutMs;

    /**
     * Create a new instance of {@link GrpcChannel}.
     *
     * @param channelKey        the channel key
     * @param channel           the grpc channel to wrap
     * @param shutdownTimeoutMs shutdown timeout in milliseconds
     */
    public GrpcChannel(GrpcChannelKey channelKey, Channel channel,
            long shutdownTimeoutMs) {
        mChannelKey = channelKey;
        mChannelHealthState = () -> mChannelHealthy;
        mChannel = ClientInterceptors.intercept(channel, new ChannelResponseTracker((this)));
        mShutdownTimeoutMs = shutdownTimeoutMs;
    }

    @Override
    public <RequestT, ResponseT> ClientCall<RequestT, ResponseT> newCall(
            MethodDescriptor<RequestT, ResponseT> methodDescriptor, CallOptions callOptions) {
        return mChannel.newCall(methodDescriptor, callOptions);
    }

    @Override
    public String authority() {
        return mChannel.authority();
    }

    /**
     * Intercepts the channel with given interceptor.
     *
     * @param interceptor interceptor
     */
    public void intercept(ClientInterceptor interceptor) {
        mChannel = ClientInterceptors.intercept(mChannel, interceptor);
    }

    /**
     * Shuts down the channel.
     */
    public synchronized void shutdown() {
        if (!mChannelReleased) {
            GrpcManagedChannelPool.INSTANCE().releaseManagedChannel(mChannelKey, mShutdownTimeoutMs);
        }
        mChannelReleased = true;
    }

    /**
     * @return {@code true} if the channel has been shut down
     */
    public synchronized boolean isShutdown() {
        return mChannelReleased;
    }

    /**
     * @return {@code true} if channel is healthy
     */
    public boolean isHealthy() {
        return mChannelHealthState.get();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ChannelKey", mChannelKey)
                .add("ChannelHealthy", mChannelHealthy)
                .add("ChannelReleased", mChannelReleased)
                .toString();
    }

    /**
     * @return a short identifier for the channel
     */
    public String toStringShort() {
        return mChannelKey.toStringShort();
    }

    /**
     * An interceptor that is used to track server calls and invalidate the channel status. Upon
     * receiving Unauthenticated or Unavailable code from the server it invalidates the channel by
     * marking it unhealthy for channel owner to be able to detect and re-authenticate or re-create
     * the channel.
     */
    private static class ChannelResponseTracker implements ClientInterceptor {
        private GrpcChannel mGrpcChannel;

        ChannelResponseTracker(GrpcChannel grpcChannel) {
            mGrpcChannel = grpcChannel;
        }

        @Override
        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                CallOptions callOptions, Channel next) {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                    next.newCall(method, callOptions)) {
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    // Put channel Id to headers.
                    super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                            responseListener) {
                        @Override
                        public void onClose(io.grpc.Status status, Metadata trailers) {
                            if (status == Status.UNAUTHENTICATED || status == Status.UNAVAILABLE) {
                                mGrpcChannel.mChannelHealthy = false;
                            }
                            super.onClose(status, trailers);
                        }
                    }, headers);
                }
            };
        }
    }
}
