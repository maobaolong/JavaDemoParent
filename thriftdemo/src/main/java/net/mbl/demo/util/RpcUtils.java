package net.mbl.demo.util;


import net.mbl.demo.Constants;
import net.mbl.demo.exception.MyException;
import net.mbl.demo.exception.UnexpectedMyException;
import net.mbl.demo.thrift.MyTException;
import net.mbl.demo.thrift.ThriftIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utilities for handling RPC calls.
 */
public final class RpcUtils {
  private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

  /**
   * Calls the given {@link RpcCallable} and handles any exceptions thrown.
   *
   * @param callable the callable to call
   * @param <T> the return type of the callable
   * @return the return value from calling the callable
   * @throws MyTException if the callable throws an runtime exception
   */
  public static <T> T call(RpcCallable<T> callable) throws MyTException {
    try {
      return callable.call();
    } catch (MyException e) {
      LOG.debug("Internal error when running rpc", e);
      throw e.toThrift();
    } catch (Exception e) {
      LOG.error("Unexpected error running rpc", e);
      throw new UnexpectedMyException(e).toThrift();
    }
  }

  /**
   * Calls the given {@link RpcCallableThrowsIOException} and handles any exceptions thrown.
   *
   * @param callable the callable to call
   * @param <T> the return type of the callable
   * @return the return value from calling the callable
   * @throws MyTException if the callable throws an or runtime exception
   * @throws ThriftIOException if the callable throws an IOException
   */
  public static <T> T call(RpcCallableThrowsIOException<T> callable)
          throws MyTException, ThriftIOException {
    try {
      return callable.call();
    } catch (MyException e) {
      LOG.debug("Internal  error when running rpc", e);
      throw e.toThrift();
    } catch (IOException e) {
      LOG.warn("I/O error when running rpc", e);
      throw new ThriftIOException(e.getMessage());
    } catch (Exception e) {
      LOG.error("Unexpected error running rpc", e);
      throw new UnexpectedMyException(e).toThrift();
    }
  }

  /**
   * An interface representing a callable which can only throw exceptions.
   *
   * @param <T> the return type of the callable
   */
  public interface RpcCallable<T> {
    /**
     * The RPC implementation.
     *
     * @return the return value from the RPC
     * @throws MyException if an expected exception occurs in the system
     */
    T call() throws MyException;
  }

  /**
   * An interface representing a callable which can only throw or IO exceptions.
   *
   * @param <T> the return type of the callable
   */
  public interface RpcCallableThrowsIOException<T> {
    /**
     * The RPC implementation.
     *
     * @return the return value from the RPC
     * @throws MyException if an expected exception occurs in the system
     * @throws IOException if an exception is thrown when interacting with the underlying system
     */
    T call() throws MyException, IOException;
  }

  private RpcUtils() {} // prevent instantiation
}
