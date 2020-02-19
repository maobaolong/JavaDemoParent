package net.mbl.grpcfull.common;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Interface for a client.
 */
public interface Client extends Closeable {

    /**
     * Connects with the remote.
     */
    void connect() throws IOException;

    /**
     * Closes the connection with the remote and does the necessary cleanup. It should be used
     * if the client has not connected with the remote for a while, for example.
     */
    void disconnect();

    /**
     * @return the {@link InetSocketAddress} of the remote
     */
    InetSocketAddress getAddress() throws IOException;

    /**
     * Returns the connected status of the client.
     *
     * @return true if this client is connected to the remote
     */
    boolean isConnected();

    /**
     * @return whether the client is closed
     */
    boolean isClosed();
}
