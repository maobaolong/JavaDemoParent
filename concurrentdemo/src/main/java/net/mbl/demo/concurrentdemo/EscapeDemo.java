package net.mbl.demo.concurrentdemo;

import sun.nio.ch.DirectBuffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * net.mbl.demo.concurrentdemo <br>
 * <p>
 * Copyright: Copyright (c) 17-3-29 下午3:41
 * <p>
 * Company: 京东
 * <p>
 *
 * @author maobaolong@jd.com
 * @version 1.0.0
 */
public class EscapeDemo {
  private final InnerClass mInnerClass;
  private int mValue;
  
  public EscapeDemo() {
    System.out.println("EscapeDemo constructor begin.");
    this.mInnerClass = new InnerClass(this);
    System.out.println("EscapeDemo constructor finish.");
  }
  
  public int getValue() {
    System.out.println(this.getClass().getSimpleName());
    return mValue;
  }
  public static void main(String[] args) {
    String[] aaa = "ss    rrr ww  cc".split("\\s+");
    sun.nio.ch.DirectBuffer db = (DirectBuffer) ByteBuffer.allocateDirect(1024);
    Class clazz = null;
    try {
      clazz = Class.forName("sun.misc.Cleaner");
    } catch (ClassNotFoundException e) {
      try {
        clazz = Class.forName("jdk.internal.ref.Cleaner");
      } catch (ClassNotFoundException e1) {
      }
    }

    try {
      if (clazz != null) {
        Method cleanMethod = clazz.getMethod("clean");
        cleanMethod.invoke(db.cleaner());
      }
    } catch (NoSuchMethodException e) {
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    EscapeDemo escapeDemo = new EscapeDemo();
    escapeDemo.getValue();
    System.out.println("main exit!");
  }
  
  class InnerClass{
    public InnerClass(EscapeDemo escapeDemo) {
      System.out.println(escapeDemo.getValue());
    }
  }
}
