package net.mbl.demo;

/**
 * Created by mbl on 10/07/2018.
 */
public class InterruptDemo {
  public static void main(String[] args) {
    Thread s = new Thread() {
      public void run() {
        while(true) {
          System.out.println("s");
          long startTime = System.currentTimeMillis();
          while (System.currentTimeMillis() - startTime < 3000L) {

          }
          System.out.println("e");
          if(Thread.currentThread().isInterrupted()) {
            System.out.println("Yes,I am interruted,but I am still running");
          }
          try {
            Thread.sleep(10000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };
    s.start();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    s.interrupt();
    System.out.println("interrupt");
  }
}
