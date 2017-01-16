package net.mbl.demo.exception;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Exception used when a network connection failure occurs. Examples include network connection
 * timeout, connection refuse, host unreachable.
 *
 */
@ThreadSafe
public final class ConnectionFailedException extends MyException {
  private static final long serialVersionUID = 3160271300708523896L;

  /**
   * Constructs a new exception with the specified detail message.
   *
   * @param message the detail message
   */
  public ConnectionFailedException(String message) {
    super(message);
  }

}
