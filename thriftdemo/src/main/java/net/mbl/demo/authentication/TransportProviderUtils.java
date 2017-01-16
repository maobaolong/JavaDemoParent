package net.mbl.demo.authentication;

import org.apache.thrift.transport.TSocket;

import javax.annotation.concurrent.ThreadSafe;
import java.net.InetSocketAddress;

/**
 * This class provides util methods for {@link TransportProvider}s.
 */
@ThreadSafe
public final class TransportProviderUtils {

  /**
   * Creates a new Thrift socket that will connect to the given address.
   *
   * @param address The given address to connect
   * @param timeoutMs the timeout in milliseconds
   * @return An unconnected socket
   */
  public static TSocket createThriftSocket(InetSocketAddress address, int timeoutMs) {
    return new TSocket(address.getAddress().getCanonicalHostName(), address.getPort(), timeoutMs);
  }

  private TransportProviderUtils() {} // prevent instantiation
}
