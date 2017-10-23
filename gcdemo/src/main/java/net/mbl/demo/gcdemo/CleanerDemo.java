package net.mbl.demo.gcdemo;

import sun.misc.Cleaner;

/**
 * Created by mbl on 17/10/2017.
 */
public class CleanerDemo {
  public static void main(String[] args) {
    // 实例化一个对象a，然后a 指向 null，a只有被影子引用的子类cleaner指向，
    // 因此gc回收时，会调用cleaner的clean方法，间接调用到Deallocator的run方法。
    A a = new A();
    a = null;
    System.gc();
    System.out.println("all done.");
  }
}

class A{
  private final Cleaner cleaner;

  public A() {
    cleaner = Cleaner.create(this, new Deallocator());
  }

  private static class Deallocator
      implements Runnable {
    @Override
    public void run() {
      System.out.println("deallocator run!");
    }
  }
}