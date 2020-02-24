package net.mbl.grpcfull.common.master;

import net.mbl.grpcfull.common.grpc.GrpcChannel;
import net.mbl.grpcfull.common.grpc.GrpcChannelBuilder;
import net.mbl.grpcfull.common.grpc.GrpcServerAddress;
import net.mbl.grpcfull.common.proto.ServiceVersionClientServiceGrpc;
import net.mbl.grpcfull.common.proto.VersionProto;
import net.mbl.grpcfull.common.retry.ExponentialBackoffRetry;
import net.mbl.grpcfull.common.retry.RetryPolicy;

import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * PollingMasterInquireClient finds the address of the primary master by polling a list of master
 * addresses to see if their RPC servers are serving. This works because only primary masters serve
 * RPCs.
 */
public class PollingMasterInquireClient implements MasterInquireClient {
    private static final Logger LOG = LoggerFactory.getLogger(PollingMasterInquireClient.class);

    private final MultiMasterConnectDetails mConnectDetails;
    private final Supplier<RetryPolicy> mRetryPolicySupplier;

    /**
     * @param masterAddresses the potential master addresses
     */
    public PollingMasterInquireClient(List<InetSocketAddress> masterAddresses) {
        this(masterAddresses, () -> new ExponentialBackoffRetry(20, 2000, 30));
    }

    /**
     * @param masterAddresses     the potential master addresses
     * @param retryPolicySupplier the retry policy supplier
     */
    public PollingMasterInquireClient(List<InetSocketAddress> masterAddresses,
            Supplier<RetryPolicy> retryPolicySupplier) {
        mConnectDetails = new MultiMasterConnectDetails(masterAddresses);
        mRetryPolicySupplier = retryPolicySupplier;
    }

    @Override
    public InetSocketAddress getPrimaryRpcAddress() throws IOException {
        RetryPolicy retry = mRetryPolicySupplier.get();
        while (retry.attempt()) {
            InetSocketAddress address = getAddress();
            if (address != null) {
                return address;
            }
        }
        throw new IOException(String.format(
                "Failed to determine primary master rpc address after polling each of %s %d times",
                mConnectDetails.getAddresses(), retry.getAttemptCount()));
    }

    @Nullable
    private InetSocketAddress getAddress() {
        // Iterate over the masters and try to connect to each of their RPC ports.
        for (InetSocketAddress address : mConnectDetails.getAddresses()) {
            try {
                LOG.debug("Checking whether {} is listening for RPCs", address);
                pingMetaService(address);
                LOG.debug("Successfully connected to {}", address);
                return address;
            } catch (IOException e) {
                LOG.debug("Failed to connect to {}", address);
                continue;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void pingMetaService(InetSocketAddress address) throws IOException {
        GrpcChannel channel =
                GrpcChannelBuilder.newBuilder(GrpcServerAddress.create(address))
                        .setClientType("MasterInquireClient").build();
        ServiceVersionClientServiceGrpc.ServiceVersionClientServiceBlockingStub versionClient =
                ServiceVersionClientServiceGrpc.newBlockingStub(channel);
        try {
            versionClient.getServiceVersion(VersionProto.GetServiceVersionPRequest.newBuilder()
                    .build());
        } catch (StatusRuntimeException e) {
            throw new IOException("status runtime excpetion", e);
        } finally {
            channel.shutdown();
        }
    }

    @Override
    public List<InetSocketAddress> getMasterRpcAddresses() {
        return mConnectDetails.getAddresses();
    }

    @Override
    public ConnectDetails getConnectDetails() {
        return mConnectDetails;
    }

    /**
     * Details used to connect to the leader master when there are multiple potential leaders.
     */
    public static class MultiMasterConnectDetails implements ConnectDetails {
        private final List<InetSocketAddress> mAddresses;

        /**
         * @param addresses a list of addresses
         */
        public MultiMasterConnectDetails(List<InetSocketAddress> addresses) {
            mAddresses = addresses;
        }

        /**
         * @return the addresses
         */
        public List<InetSocketAddress> getAddresses() {
            return mAddresses;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MultiMasterConnectDetails)) {
                return false;
            }
            MultiMasterConnectDetails that = (MultiMasterConnectDetails) o;
            return mAddresses.equals(that.mAddresses);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mAddresses);
        }

        @Override
        public String toString() {
            return "---";
        }
    }
}
