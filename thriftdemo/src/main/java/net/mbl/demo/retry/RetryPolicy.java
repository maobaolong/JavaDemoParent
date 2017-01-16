package net.mbl.demo.retry;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Attempts to retry code from a do/while loop. The way that this interface works is that the logic
 * for delayed retries (retries that sleep) can delay the caller of {@link #attemptRetry()}. Because
 * of this, its best to put retries in do/while loops to avoid the first wait.
 */
@NotThreadSafe
public interface RetryPolicy {

  /**
   * How many retries have been performed. If no retries have been performed, 0 is returned.
   *
   * @return number of retries performed
   */
  int getRetryCount();

  /**
   * Attempts to run the given operation, returning false if unable to (max retries have happened).
   *
   * @return whether the operation have succeeded or failed (max retries have happened)
   */
  boolean attemptRetry();
}
