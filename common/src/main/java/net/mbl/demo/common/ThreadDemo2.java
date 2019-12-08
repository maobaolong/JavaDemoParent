package net.mbl.demo.common;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadDemo2 {
  static AtomicInteger index = new AtomicInteger(0);
  public static class ThreadX extends Thread {

    private final boolean b;
    private ThreadX friend;
    Object lock = new Object();

    public ThreadX(boolean b, String name) {
      this.setName(name);
      this.b = b;
    }
    
    public void setFriend(ThreadX t) {
      this.friend = t;
    }
    @Override
    public void run() {
      for (int i = 0; i < 100; i++) {
        if (this.b) {
          while (index.get() % 2 != 0) {
            
          }
          if (i % 2 == 0) {
            System.out.println(getName() + "：" + i);
            index.incrementAndGet();
          }
        } else {
          while (index.get() % 2 == 0) {
            
          }
          if (i % 2 != 0) {
            System.out.println(getName() + "：" + i);
            index.incrementAndGet();
          }
        }
        
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadX t1 = new ThreadX(true, "A");
    ThreadX t2 = new ThreadX(false, "B");
    t1.setFriend(t2);
    t2.setFriend(t1);
    t1.start();
    Thread.sleep(1000);
    t2.start();
  }

}
