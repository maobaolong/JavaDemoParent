package net.mbl.demo.btrace;

import java.util.concurrent.TimeUnit;

public class BTraceOnMethodDemo {

  public void say() {}
  public static void main(String[] args) {
    final BTraceOnMethodDemo demo = new BTraceOnMethodDemo();
    try {
      TimeUnit.SECONDS.sleep(15);    
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  System.out.println("start main method...");
  new Thread(new Runnable() {
    @Override
    public void run() {
      while (true) {
        try {
          TimeUnit.SECONDS.sleep(1);
          demo.say();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        }
      }
    }).start();
  }
}
