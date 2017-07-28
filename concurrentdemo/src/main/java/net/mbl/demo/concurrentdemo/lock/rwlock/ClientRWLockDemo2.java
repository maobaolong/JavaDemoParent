package net.mbl.demo.concurrentdemo.lock.rwlock;

import java.util.concurrent.locks.Lock;

public class ClientRWLockDemo2 {
  private final static ClientRWLock clientRWLock = new ClientRWLock();
  public static void main(String[] args) {
    Lock lock = clientRWLock.readLock();
    lock.lock();
    clientRWLock.writeLock().lock();
    System.out.println("end");

  }
}
