package net.mbl.demo.concurrentdemo;

import sun.misc.GC;

/**
 * Created by mbl on 22/12/2017.
 */
public class ThreadSafeDemo {
  Object obj = new Object() {
    public String toString() {
     for (int i=0; i<3; i++) {
       try {
         Thread.sleep(1000);
       } catch (InterruptedException e) {
         e.printStackTrace();
       }
       System.out.println(i + ":" + super.toString());
     }
     return super.toString();
    }
  };

  public String test() {
    return obj.toString();
  }
  public static void main(String[] args) throws InterruptedException {
    ThreadSafeDemo threadSafeDemo = new ThreadSafeDemo();
    System.out.println(Thread.currentThread().getName() + " : " + threadSafeDemo.test());
    Thread t = new Thread("testThread") {
      public void run() {
        while(true) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println(this.getName() + " : " + threadSafeDemo.test());
        }
      }
    };

    t.start();

    Thread.sleep(5000);
    threadSafeDemo.obj = new Object();
    System.gc();
    Thread.sleep(5000);
    System.out.println(Thread.currentThread().getName() + " : " + threadSafeDemo.test());
  }
}
