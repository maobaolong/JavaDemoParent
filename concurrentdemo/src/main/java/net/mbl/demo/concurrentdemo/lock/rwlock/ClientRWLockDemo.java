package net.mbl.demo.concurrentdemo.lock.rwlock;

import java.util.concurrent.locks.Lock;

public class ClientRWLockDemo {
  private final static ClientRWLock clientRWLock = new ClientRWLock();
  public static void main(String[] args) {
    
    new Thread("a") {
      public void run(){
        for (int i=0; i < 100; i++) {
          Lock lock = clientRWLock.readLock();
          System.out.println(getName() + " want to get readLock");
          lock.lock();
          System.out.println(getName() + " has got readLock");
          lock.unlock();
        }
      }
    }.start();
    new Thread("b") {
      public void run(){
        for (int i=0; i < 100; i++) {
          Lock lock = clientRWLock.writeLock();
          System.out.println(getName() + " want to get writeLock");
          lock.lock();
          System.out.println(getName() + " has got writeLock");
          lock.unlock();
        }
      }
    }.start();
    new Thread("c") {
      public void run(){
        for (int i=0; i < 100; i++) {
          Lock lock = clientRWLock.readLock();
          System.out.println(getName() + " want to get readLock");
          lock.lock();
          System.out.println(getName() + " has got readLock");
          lock.unlock();
        }
      }
    }.start();
    new Thread("d") {
      public void run(){
        for (int i=0; i < 100; i++) {
          Lock lock = clientRWLock.readLock();
          System.out.println(getName() + " want to get readLock");
          lock.lock();
          System.out.println(getName() + " has got readLock");
          lock.unlock();
        }
      }
    }.start();
    new Thread("e") {
      public void run(){
        for (int i=0; i < 100; i++) {
          Lock lock = clientRWLock.readLock();
          System.out.println(getName() + " want to get readLock");
          lock.lock();
          System.out.println(getName() + " has got readLock");
          lock.unlock();
        }
      }
    }.start();
    

    try {
      Thread.sleep(10000000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


  }
}
