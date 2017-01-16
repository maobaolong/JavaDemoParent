package net.mbl.demo.client;


import net.mbl.demo.exception.ConnectionFailedException;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for a client in the system.
 */
public interface Client extends Closeable {

  /**
   * Connects with the remote.
   *
   * @throws IOException if an I/O error occurs
   * @throws ConnectionFailedException if network connection failed
   */
  void connect() throws IOException, ConnectionFailedException;

  /**
   * Closes the connection, then queries and sets current remote address.
   */
  void resetConnection();

  /**
   * Returns the connected status of the client.
   *
   * @return true if this client is connected to the remote
   */
  boolean isConnected();

  /**
   * Closes the connection with the remote permanently. This instance should be not be reused after
   * closing.
   */
  void close();
}
