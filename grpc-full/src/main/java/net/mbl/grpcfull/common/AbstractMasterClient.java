package net.mbl.grpcfull.common;

import net.mbl.grpcfull.common.master.MasterInquireClient;
import net.mbl.grpcfull.common.retry.RetryPolicy;

import com.google.common.collect.Lists;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Supplier;

/**
 * The base class for master clients.
 */
@ThreadSafe
public abstract class AbstractMasterClient extends AbstractClient {
    /**
     * Client for determining the master RPC address.
     */
    private final MasterInquireClient mMasterInquireClient;

    /**
     * Creates a new master client base.
     */
    public AbstractMasterClient(InetSocketAddress address,
            Supplier<RetryPolicy> retryPolicySupplier) {
        super(address, retryPolicySupplier);
        mMasterInquireClient =  MasterInquireClient.Factory.create(Lists.newArrayList(address));
    }

    public AbstractMasterClient(InetSocketAddress address) {
        super(address);
        mMasterInquireClient =  MasterInquireClient.Factory.create(Lists.newArrayList(address));
    }

    @Override
    public synchronized InetSocketAddress getAddress() throws IOException {
        return mMasterInquireClient.getPrimaryRpcAddress();
    }
}
