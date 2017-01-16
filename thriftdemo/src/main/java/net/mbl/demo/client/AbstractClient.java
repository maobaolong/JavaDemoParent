
package net.mbl.demo.client;



import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import net.mbl.demo.Constants;
import net.mbl.demo.authentication.TransportProvider;
import net.mbl.demo.exception.MyException;
import net.mbl.demo.exception.ConnectionFailedException;
import net.mbl.demo.retry.ExponentialBackoffRetry;
import net.mbl.demo.retry.RetryPolicy;
import net.mbl.demo.thrift.MyTException;
import net.mbl.demo.thrift.HelloWorldService;
import net.mbl.demo.thrift.ThriftIOException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import javax.annotation.concurrent.ThreadSafe;
import javax.security.auth.Subject;

/**
 * The base class for clients.
 */
// TODO(peis): Consolidate this to ThriftClientPool.
@ThreadSafe
public abstract class AbstractClient implements Client {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractClient.class.getName());

  /** The pattern of exception message when client and server transport frame sizes do not match. */
  private static final Pattern FRAME_SIZE_EXCEPTION_PATTERN =
      Pattern.compile("Frame size \\((\\d+)\\) larger than max length");

  /** The number of times to retry a particular RPC. */
  protected static final int RPC_MAX_NUM_RETRY = 30;

  protected final String mMode;

  protected InetSocketAddress mAddress = null;
  protected TProtocol mProtocol = null;

  /** Is true if this client is currently connected. */
  protected boolean mConnected = false;

  /**
   * Is true if this client was closed by the user. No further actions are possible after the client
   * is closed.
   */
  protected boolean mClosed = false;

  /**
   * Stores the service version; used for detecting incompatible client-server pairs.
   */
  protected long mServiceVersion;

  /** Handler to the transport provider according to the authentication type. */
  protected final TransportProvider mTransportProvider;

  private final Subject mParentSubject;

  /**
   * Creates a new client base.
   *
   * @param subject the parent subject, set to null if not present
   * @param address the address
   * @param mode the mode of the client for display
   */
  public AbstractClient(Subject subject, InetSocketAddress address, String mode) {
    mAddress = Preconditions.checkNotNull(address);
    mMode = mode;
    mServiceVersion = Constants.UNKNOWN_SERVICE_VERSION;
    mTransportProvider = TransportProvider.Factory.create();
    mParentSubject = subject;
  }

  /**
   * @return a Thrift service client
   */
  protected abstract HelloWorldService.Client getClient();

  /**
   * @return a string representing the specific service
   */
  protected abstract String getServiceName();

  /**
   * @return the client service version
   */
  protected abstract long getServiceVersion();

  /**
   * Checks that the service version is compatible with the client.
   *
   * @param client the service client
   * @param version the client version
   */
  protected void checkVersion(HelloWorldService.Client client, long version) throws IOException {
//    if (mServiceVersion == Constants.UNKNOWN_SERVICE_VERSION) {
//      try {
//        mServiceVersion = client.getServiceVersion();
//      } catch (TException e) {
//        throw new IOException(e);
//      }
//      if (mServiceVersion != version) {
//        throw new IOException(ExceptionMessage.INCOMPATIBLE_VERSION.getMessage(getServiceName(),
//            version, mServiceVersion));
//      }
//    }
  }

  /**
   * This method is called after the connection is made to the remote. Implementations should create
   * internal state to finish the connection process.
   */
  protected void afterConnect() throws IOException {
    // Empty implementation.
  }

  /**
   * This method is called after the connection is disconnected. Implementations should clean up any
   * additional state created for the connection.
   */
  protected void afterDisconnect() {
    // Empty implementation.
  }

  /**
   * This method is called before the connection is disconnected. Implementations should add any
   * additional operations before the connection is disconnected.
   */
  protected void beforeDisconnect() {
    // Empty implementation.
  }

  /**
   * Connects with the remote.
   *
   * @throws IOException if an I/O error occurs
   * @throws ConnectionFailedException if network connection failed
   */
  public synchronized void connect() throws IOException, ConnectionFailedException {
    if (mConnected) {
      return;
    }
    disconnect();
    Preconditions.checkState(!mClosed, "Client is closed, will not try to connect.");

    int maxConnectsTry = 29;
    final int BASE_SLEEP_MS = 50;
    RetryPolicy retry =
        new ExponentialBackoffRetry(BASE_SLEEP_MS, Constants.SECOND_MS, maxConnectsTry);
    while (!mClosed) {
      mAddress = getAddress();
      LOG.info(" client (version {}) is trying to connect with {} {} @ {}",
          "1.0", getServiceName(), mMode, mAddress);

      TProtocol binaryProtocol =
          new TBinaryProtocol(mTransportProvider.getClientTransport(mParentSubject, mAddress));
      mProtocol = new TMultiplexedProtocol(binaryProtocol, getServiceName());
      try {
        mProtocol.getTransport().open();
        LOG.info("Client registered with {} {} @ {}", getServiceName(), mMode, mAddress);
        mConnected = true;
        afterConnect();
        checkVersion(getClient(), getServiceVersion());
        return;
      } catch (IOException e) {
        if (e.getMessage() != null && FRAME_SIZE_EXCEPTION_PATTERN.matcher(e.getMessage()).find()) {
          // See an error like "Frame size (67108864) larger than max length (16777216)!",
          // pointing to the helper page.
          String message = String.format("Failed to connect to %s %s @ %s: %s. "
              + "This exception may be caused by incorrect network configuration. "
              + "Please consult %s for common solutions to address this problem.",
              getServiceName(), mMode, mAddress, e.getMessage(),
              "http://baidu.com");
          throw new IOException(message, e);
        }
        throw e;
      } catch (TTransportException e) {
        LOG.error("Failed to connect (" + retry.getRetryCount() + ") to " + getServiceName() + " "
            + mMode + " @ " + mAddress + " : " + e.getMessage());
        if (e.getCause() instanceof java.net.SocketTimeoutException) {
          // Do not retry if socket timeout.
          String message = "Thrift transport open times out. Please check whether the "
              + "authentication types match between client and server. Note that NOSASL client "
              + "is not able to connect to servers with SIMPLE security mode.";
          throw new IOException(message, e);
        }
        // TODO(peis): Consider closing the connection here as well.
        if (!retry.attemptRetry()) {
          break;
        }
      }
    }
    // Reaching here indicates that we did not successfully connect.
    throw new ConnectionFailedException("Failed to connect to " + getServiceName() + " " + mMode
        + " @ " + mAddress + " after " + (retry.getRetryCount()) + " attempts");
  }

  /**
   * Closes the connection with the remote and does the necessary cleanup. It should be used
   * if the client has not connected with the remote for a while, for example.
   */
  public synchronized void disconnect() {
    if (mConnected) {
      LOG.debug("Disconnecting from the {} {} {}", getServiceName(), mMode, mAddress);
      beforeDisconnect();
      mProtocol.getTransport().close();
      mConnected = false;
      afterDisconnect();
    }
  }

  /**
   * @return true if this client is connected to the remote
   */
  public synchronized boolean isConnected() {
    return mConnected;
  }

  /**
   * Closes the connection with the remote permanently. This instance should be not be reused after
   * closing.
   */
  @Override
  public synchronized void close() {
    disconnect();
    mClosed = true;
  }

  /**
   * Closes the connection, then queries and sets current remote address.
   */
  public synchronized void resetConnection() {
    disconnect();
    mAddress = getAddress();
  }

  /**
   * @return the {@link InetSocketAddress} of the remote
   */
  protected synchronized InetSocketAddress getAddress() {
    return mAddress;
  }

  /**
   * The RPC to be executed in {@link #retryRPC(RpcCallable)}.
   *
   * @param <V> the return value of {@link #call()}
   */
  protected interface RpcCallable<V> {
    /**
     * The task where RPC happens.
     *
     * @return RPC result
     * @throws TException when any exception defined in thrift happens
     */
    V call() throws TException;
  }

  /**
   * Same with {@link RpcCallable} except that this RPC call throws {@link MyTException} and
   * is to be executed in {@link #retryRPC(RpcCallableThrowsMyTException)}.
   *
   * @param <V> the return value of {@link #call()}
   */
  protected interface RpcCallableThrowsMyTException<V> {
    /**
     * The task where RPC happens.
     *
     * @return RPC result
     * @throws MyTException when any {@link MyException} happens during RPC and is wrapped
     *         into {@link MyTException}
     * @throws TException when any exception defined in thrift happens
     */
    V call() throws MyTException, TException;
  }

  /**
   * Tries to execute an RPC defined as a {@link RpcCallable}.
   *
   * If a non-my thrift exception occurs, a reconnection will be tried through
   * {@link #connect()} and the action will be re-executed.
   *
   * @param rpc the RPC call to be executed
   * @param <V> type of return value of the RPC call
   * @return the return value of the RPC call
   * @throws IOException when retries exceeds {@link #RPC_MAX_NUM_RETRY} or {@link #close()} has
   *         been called before calling this method or during the retry
   * @throws ConnectionFailedException if network connection failed
   */
  protected synchronized <V> V retryRPC(RpcCallable<V> rpc) throws IOException,
      ConnectionFailedException {
    int retry = 0;
    while (!mClosed && (retry++) <= RPC_MAX_NUM_RETRY) {
      connect();
      try {
        return rpc.call();
      } catch (ThriftIOException e) {
        throw new IOException(e);
      } catch (MyTException e) {
        throw Throwables.propagate(MyException.fromThrift(e));
      } catch (TException e) {
        LOG.error(e.getMessage(), e);
        disconnect();
      }
    }
    throw new IOException("Failed after " + retry + " retries.");
  }

  /**
   * Similar to {@link #retryRPC(RpcCallable)} except that the RPC call may throw
   * {@link MyTException} and once it is thrown, it will be transformed into
   * {@link MyException} and be thrown.
   *
   * @param rpc the RPC call to be executed
   * @param <V> type of return value of the RPC call
   * @return the return value of the RPC call
   * @throws MyException when {@link MyTException} is thrown by the RPC call
   * @throws IOException when retries exceeds {@link #RPC_MAX_NUM_RETRY} or {@link #close()} has
   *         been called before calling this method or during the retry
   */
  protected synchronized <V> V retryRPC(RpcCallableThrowsMyTException<V> rpc)
      throws MyException, IOException {
    int retry = 0;
    while (!mClosed && (retry++) <= RPC_MAX_NUM_RETRY) {
      connect();
      try {
        return rpc.call();
      } catch (MyTException e) {
        throw MyException.fromThrift(e);
      } catch (ThriftIOException e) {
        throw new IOException(e);
      } catch (TException e) {
        LOG.error(e.getMessage(), e);
        disconnect();
      }
    }
    throw new IOException("Failed after " + retry + " retries.");
  }
}
