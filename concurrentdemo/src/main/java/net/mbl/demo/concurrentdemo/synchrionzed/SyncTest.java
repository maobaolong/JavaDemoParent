    package net.mbl.demo.concurrentdemo.synchrionzed;

    /**
     * net.mbl.demo.concurrentdemo.synchrionzed <br>
     *
     * @author maobaolong@139.com
     * @version 1.0.0
     */
    public class SyncTest {

      public static void main(String[] args) {
        final B b = new B();
        // A synchronized method in the superclass acquires the same lock as one in the subclass
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.myOneMethod();
            a--;
          }
        }.start();
        // Two synchronized method in the same instance acquires the same lock. In different critical
        // region.
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.myTwoMethod();
            a--;
          }
        }.start();
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.myThreeMethod();
            a--;
          }
        }.start();
        // A synchronized region in the same thread can re-entered.
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.myFourMethod();
            a--;
          }
        }.start();
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.myFiveMethod();
            a--;
          }
        }.start();
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.mySixMethod();
            a--;
          }
        }.start();
        new Thread(){
          public void run(){
            int a=2;
            a++;
            b.mySevenMethod();
            a--;
          }
        }.start();
      }
    }

    class A {
      public synchronized void myOneMethod(){
        System.out.println("{ myOneMethod");
        for(int i = 0; i < 10; i++){
          System.out.println("myOneMethod " + i);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("} myOneMethod");
      }
    }
    class B extends A {
      public synchronized void myTwoMethod(){
        System.out.println("{ myTwoMethod");
        for(int i = 0; i < 10; i++){
          System.out.println("myTwoMethod " + i);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("} myTwoMethod");
      }
      public synchronized void myThreeMethod(){
        System.out.println("{ myThreeMethod");
        for(int i = 0; i < 10; i++){
          System.out.println("myThreeMethod " + i);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("} myThreeMethod");
      }
      public synchronized  void myFourMethod(){
        System.out.println("{ myFourMethod");
        myOneMethod();
        System.out.println("} myFourMethod");
      }
      public void myFiveMethod(){
        System.out.println("{ myFiveMethod");
        myThreeMethod();
        System.out.println("} myFiveMethod");
      }
      public void mySixMethod(){
        System.out.println("{ mySixMethod");
        for(int i = 0; i < 10; i++){
          System.out.println("mySixMethod " + i);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        System.out.println("} mySixMethod");
      }
      public synchronized void mySevenMethod(){
        System.out.println("{ mySevenMethod");
        mySixMethod();
        System.out.println("} mySevenMethod");
      }
    }

