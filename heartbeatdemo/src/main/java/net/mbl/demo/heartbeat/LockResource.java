package net.mbl.demo.heartbeat;

import java.util.concurrent.locks.Lock;

public class LockResource implements AutoCloseable {
  private final Lock mLock;

  /**
   * Creates a new instance of {@link LockResource} using the given lock.
   *
   * @param lock the lock to acquire
   */
  public LockResource(Lock lock) {
    mLock = lock;
    mLock.lock();
  }

  /**
   * Releases the lock.
   */
  @Override
  public void close() {
    mLock.unlock();
  }
}