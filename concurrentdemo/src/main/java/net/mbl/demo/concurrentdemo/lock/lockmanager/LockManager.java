package net.mbl.demo.concurrentdemo.lock.lockmanager;

import java.lang.management.ThreadInfo;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static net.mbl.demo.concurrentdemo.lock.lockmanager.ThreadUtilities.getLockingThread;
import static net.mbl.demo.concurrentdemo.lock.lockmanager.ThreadUtilities.getThread;
import static net.mbl.demo.concurrentdemo.lock.lockmanager.ThreadUtilities.getThreadInfo;

/**
 * Created by mbl on 28/07/2017.
 */
public class LockManager {
  private static final LockManager INSTANCE = new LockManager();
  public static LockManager getInstance() {
    return INSTANCE;
  }
  ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();

  Thread t3 = new Thread("T3"){
    public void run(){
      mLock.writeLock().lock();
      mLock.readLock().lock();
      try {
        int a = 0;
        while(a == 0){
          a++;
          a--;
        }
        System.out.println("T3 readlock unlocked");
      } finally {
        mLock.readLock().unlock();
      }


      System.out.println("T3 pass");
    }
  };

  Thread t4 = new Thread("T4"){
    public void run(){
      try {

        mLock.readLock().lock();
        System.out.println("T4 readlock accquired");
        int a = 0;
        while(a == 0){
          a++;
          a--;
        }
      } finally {
//        mLock.readLock().unlock();
      }


      System.out.println("T3 pass");
    }
  };

  public static void main(String[] args) throws InterruptedException {
    LockManager lockManager = LockManager.getInstance();
    lockManager.t3.start();
    lockManager.t4.start();
    Thread.sleep(1000);

  }
}
