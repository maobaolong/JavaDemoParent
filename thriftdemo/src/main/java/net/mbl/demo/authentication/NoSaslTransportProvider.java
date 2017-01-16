package net.mbl.demo.authentication;


import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportFactory;

import javax.annotation.concurrent.ThreadSafe;
import javax.security.auth.Subject;
import javax.security.sasl.SaslException;
import java.net.InetSocketAddress;

/**
 * If authentication type is {@link AuthType#NOSASL}, we use this transport provider which simply
 * uses default Thrift {@link TFramedTransport}.
 */
@ThreadSafe
public final class NoSaslTransportProvider implements TransportProvider {
  /** Timeout for socket in ms. */
  private final int mSocketTimeoutMs;
  /** Max frame size of thrift transport in bytes. */
  private final int mThriftFrameSizeMax;

  /**
   * Constructor for transport provider when authentication type is {@link AuthType#NOSASL}.
   */
  public NoSaslTransportProvider() {
    mSocketTimeoutMs = 0;//600000;//Configuration.getInt(PropertyKey.SECURITY_AUTHENTICATION_SOCKET_TIMEOUT_MS);
    mThriftFrameSizeMax =16*1024*1024;/*
        (int) Configuration.getBytes(PropertyKey.NETWORK_THRIFT_FRAME_SIZE_BYTES_MAX);*/
  }

  @Override
  public TTransport getClientTransport(InetSocketAddress serverAddress) {
    TTransport tTransport =
        TransportProviderUtils.createThriftSocket(serverAddress, mSocketTimeoutMs);
    return new TFramedTransport(tTransport, mThriftFrameSizeMax);
  }

  @Override
  public TTransport getClientTransport(Subject subject, InetSocketAddress serverAddress) {
    TTransport tTransport =
        TransportProviderUtils.createThriftSocket(serverAddress, mSocketTimeoutMs);
    return new TFramedTransport(tTransport, mThriftFrameSizeMax);
  }

  @Override
  public TTransportFactory getServerTransportFactory() throws SaslException {
    return new TFramedTransport.Factory(mThriftFrameSizeMax);
  }

  @Override
  public TTransportFactory getServerTransportFactory(Runnable runnable) throws SaslException {
    return new TFramedTransport.Factory(mThriftFrameSizeMax);
  }
}
