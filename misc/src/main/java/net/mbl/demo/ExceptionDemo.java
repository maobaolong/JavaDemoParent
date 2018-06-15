package net.mbl.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mbl on 20/04/2018.
 */
public class ExceptionDemo {
  private static final Logger LOG = LoggerFactory.getLogger("ExceptionDemo");
  public static void func() {
    Object o = null;
    o.toString();
  }

  public static void func2() {
    func();
  }
  public static void main(String[] args) throws InterruptedException {
    final Object o = null;
    for (int i = 0; i < 1000000000; i++) {
      try {
//        LOG.info("" + i);
//        o.toString();
        args[0].toString();

//        func2();
//        o
      } catch (Exception e) {

        if (e.getStackTrace().length == 0) {
          System.out.format("Java ate my stacktrace after iteration #%d %n", i);
          break;
        } else {

        }
//        LOG.warn("" + i, e);
      }
    }
/*    while(true) {
      func2();
      Thread.sleep(10);
    }*/
  }
}
