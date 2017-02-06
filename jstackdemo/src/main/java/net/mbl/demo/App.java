package net.mbl.demo;

/**
 * Hello world!
 */
public class App {
  private static void RUNNABLE() {
    Thread t = new Thread() {
      public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
          System.out.println(i);
        }
      }
    };
    t.start();
  }

  private static void BLOCKED() {
    final Object lock = new Object();
    Runnable run = new Runnable() {
      public void run() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
          synchronized (lock) {
            System.out.println(i);
          }
        }
      }
    };

    Thread t1 = new Thread(run);
    t1.setName( "t1");
    Thread t2 = new Thread(run);
    t2.setName( "t2");

    t1.start();
    t2.start();
  }
  private static void WAITING() {
    final Object lock = new Object();
    Thread t1 = new Thread(){
      @Override
      public void run() {
        int i = 0;
        while(true ){
          synchronized (lock) {
            try {
              lock.wait();
            } catch (InterruptedException e) {
            }
            System. out.println(i++);
          }
        }
      }
    };

    Thread t2 = new Thread(){
      @Override
      public void run() {
        while(true ){
          synchronized (lock) {
            for(int i = 0; i< 10000000; i++){
              System. out.println(i);
            }
            lock.notifyAll();
          }
        }
      }
    };

    t1.setName( "^^t1^^");
    t2.setName( "^^t2^^");

    t1.start();
    t2.start();
  }
  public static void main(String[] args) {
    System.out.println("Hello World!");
//    RUNNABLE();
//    BLOCKED();
    WAITING();
  }
}
