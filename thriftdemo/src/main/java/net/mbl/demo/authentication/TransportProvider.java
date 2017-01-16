package net.mbl.demo.authentication;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;

import javax.security.auth.Subject;
import javax.security.sasl.SaslException;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Interface to provide thrift transport service for thrift client and server, based on the type
 * of authentication.
 */
public interface TransportProvider {
  /**
   * Factory for {@code TransportProvider}.
   */
  class Factory {

    // prevent instantiation
    private Factory() {}

    /**
     * Creates a new instance of {@code TransportProvider} based on authentication type. For
     * {@link AuthType#NOSASL}, return an instance of {@link NoSaslTransportProvider}; for
     * {@link AuthType#SIMPLE} or {@link AuthType#CUSTOM}, return an instance of
     *
     * @return the generated {@link TransportProvider}
     */
    public static TransportProvider create() {
      AuthType authType = AuthType.NOSASL;
      switch (authType) {
        case NOSASL:
          return new NoSaslTransportProvider();
        case SIMPLE: // intended to fall through
//        case CUSTOM:
//          return new PlainSaslTransportProvider();
        case KERBEROS:
          throw new UnsupportedOperationException(
              "getClientTransport: Kerberos is not supported currently.");
        default:
          throw new UnsupportedOperationException(
              "getClientTransport: Unsupported authentication type: " + authType.getAuthName());
      }
    }
  }

  /**
   * Creates a transport per the connection options. Supported transport options are:
   * {@link AuthType#NOSASL}, {@link AuthType#SIMPLE}, {link@ AuthType#CUSTOM},
   * {@link AuthType#KERBEROS}. With NOSASL as input, an unmodified {@link TTransport} is returned;
   * with SIMPLE/CUSTOM as input, a PlainClientTransport is returned; KERBEROS is not supported
   * currently. If the auth type is not supported or recognized, an
   * {@link UnsupportedOperationException} is thrown.
   *
   * @param serverAddress the server address which clients will connect to
   * @return a TTransport for client
   * @throws IOException if building a TransportFactory fails or user login fails
   */
  TTransport getClientTransport(InetSocketAddress serverAddress) throws IOException;

  /**
   * Similar as {@link TransportProvider#getClientTransport(InetSocketAddress)} but it also
   * specifies the {@link Subject} explicitly.
   *
   * @param subject the subject, set to null if not present
   * @param serverAddress the server address which clients will connect to
   * @return a TTransport for client
   * @throws IOException if building a TransportFactory fails or user login fails
   */
  TTransport getClientTransport(Subject subject, InetSocketAddress serverAddress)
      throws IOException;

  /**
   * For server side, this method returns a {@link TTransportFactory} based on the auth type. It is
   * used as one argument to build a Thrift {@link org.apache.thrift.server.TServer}. If the auth
   * type is not supported or recognized, an {@link UnsupportedOperationException} is thrown.
   *
   * @return a corresponding TTransportFactory
   * @throws SaslException if building a TransportFactory fails
   */
  TTransportFactory getServerTransportFactory() throws SaslException;

  /**
   * For server side, this method returns a {@link TTransportFactory} based on the auth type. It is
   * used as one argument to build a Thrift {@link org.apache.thrift.server.TServer}. If the auth
   * type is not supported or recognized, an {@link UnsupportedOperationException} is thrown.
   *
   * @param runnable a closure runs after the transport is established
   * @return a corresponding TTransportFactory
   * @throws SaslException if building a TransportFactory fails
   */
  TTransportFactory getServerTransportFactory(Runnable runnable) throws SaslException;
}
