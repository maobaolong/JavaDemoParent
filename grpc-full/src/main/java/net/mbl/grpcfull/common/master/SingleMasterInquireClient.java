package net.mbl.grpcfull.common.master;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A {@link MasterInquireClient} which always returns a fixed master address.
 */
public class SingleMasterInquireClient implements MasterInquireClient {
    private final SingleMasterConnectDetails mConnectDetails;

    /**
     * @param address the master address
     */
    public SingleMasterInquireClient(InetSocketAddress address) {
        mConnectDetails = new SingleMasterConnectDetails(address);
    }

    @Override
    public InetSocketAddress getPrimaryRpcAddress() {
        return mConnectDetails.getAddress();
    }

    @Override
    public List<InetSocketAddress> getMasterRpcAddresses() {
        return Collections.singletonList(mConnectDetails.getAddress());
    }

    @Override
    public ConnectDetails getConnectDetails() {
        return mConnectDetails;
    }

    /**
     * Connect details for a single master inquire client.
     */
    public static class SingleMasterConnectDetails implements ConnectDetails {
        private final InetSocketAddress mAddress;

        /**
         * @param address an address
         */
        public SingleMasterConnectDetails(InetSocketAddress address) {
            mAddress = address;
        }

        /**
         * @return the address
         */
        public InetSocketAddress getAddress() {
            return mAddress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SingleMasterConnectDetails)) {
                return false;
            }
            SingleMasterConnectDetails that = (SingleMasterConnectDetails) o;
            return mAddress.equals(that.mAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mAddress);
        }

        @Override
        public String toString() {
            return "-----";
        }
    }
}
