package net.mbl.grpcfull.common.master;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Client for determining the primary master.
 */
@ThreadSafe
public interface MasterInquireClient {
    /**
     * @return the rpc address of the primary master. The implementation should perform retries if
     * appropriate
     * @throws IOException if the primary rpc address cannot be determined
     */
    InetSocketAddress getPrimaryRpcAddress() throws IOException;

    /**
     * @return a list of all masters' RPC addresses
     * @throws IOException if the master rpc addresses cannot be determined
     */
    List<InetSocketAddress> getMasterRpcAddresses() throws IOException;

    /**
     * Returns canonical connect details representing how this client connects to the master.
     *
     * @return the connect details
     */
    ConnectDetails getConnectDetails();

    /**
     * Interface for representing master inquire connect details.
     * <p>
     * Connect info should be unique so that if two inquire clients have the same connect info, they
     * connect to the same cluster.
     */
    interface ConnectDetails {
        @Override
        boolean equals(Object obj);

        @Override
        int hashCode();
    }

    /**
     * Factory for getting a master inquire client.
     */
    class Factory {
        /**
         * @return a master inquire client
         */
        public static MasterInquireClient create(List<InetSocketAddress> addresses) {
            if (addresses.size() > 1) {
                return new PollingMasterInquireClient(addresses);
            } else {
                return new SingleMasterInquireClient(addresses.get(0));
            }
        }

        private Factory() {
        } // Not intended for instantiation.
    }
}
