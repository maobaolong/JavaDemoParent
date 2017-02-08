package net.mbl.demo.concurrentdemo.keywordvolatile;

/**
 *
 */
public class VolatileDemo {
  static volatile int i = 0, j = 0;
  static void one() { i++; j++; }
  static void two() {
    System.out.println("i=" + i + " j=" + j);
  }

  public static void main(String[] args) {
    new Thread(){
      public void run() {
        for (int m = 0; m < 1000000; m++)
          one();
      }
    }.start();
    new Thread(){
      public void run() {
        for (int m = 0; m < 1000000; m++)
          two();
      }
    }.start();
  }
}
