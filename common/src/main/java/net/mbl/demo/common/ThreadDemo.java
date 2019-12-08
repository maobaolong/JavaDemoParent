package net.mbl.demo.common;

public class ThreadDemo {
  static Object lock = new Object();
  public static class ThreadX extends Thread {

    private final boolean b;
    
    public ThreadX(boolean b, String name) {
      this.setName(name);
      this.b = b;
    }

    @Override
    public void run() {
      for (int i = 0; i < 100; i++) {
        synchronized (lock) {
          if (this.b) {
            if (i % 2 == 0) {
              lock.notify();
              System.out.println(getName() + "：" + i);
              try {
                lock.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          } else {
            if (i % 2 != 0) {
              System.out.println(getName() + "：" + i);
              lock.notify();
              try {
                lock.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    ThreadX t1 = new ThreadX(true, "A");
    ThreadX t2 = new ThreadX(false, "B");
    t1.start();
    Thread.sleep(1000);
    t2.start();
  }

}
