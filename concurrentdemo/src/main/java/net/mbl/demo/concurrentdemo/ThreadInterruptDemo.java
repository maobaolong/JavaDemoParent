package net.mbl.demo.concurrentdemo;

/**
 * Created by mbl on 21/12/2017.
 */
public class ThreadInterruptDemo {
  public static void main(String[] args) {
    Thread testThread = new Thread () {
      int i = 0;
      public void run() {
        try {
          while(true) {
  //          for (long j=0; j < 0x80000000L; j++);

            Thread.sleep(1000);
            System.out.println(i++);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    testThread.start();
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    testThread.interrupt();
  }
}
