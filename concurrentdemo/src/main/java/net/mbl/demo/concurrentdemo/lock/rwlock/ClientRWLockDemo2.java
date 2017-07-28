package net.mbl.demo.concurrentdemo.lock.rwlock;

import java.util.concurrent.locks.Lock;

public class ClientRWLockDemo2 {
  //ClientRWLock unsupport reentrant, so unsupport lock downgrade too.
  private final static ClientRWLock clientRWLock = new ClientRWLock();
  public static void main(String[] args) {
    clientRWLock.writeLock().lock();
    clientRWLock.writeLock().lock();
    Lock lock = clientRWLock.readLock();
//    lock.lock();

    System.out.println("end");

  }
}
