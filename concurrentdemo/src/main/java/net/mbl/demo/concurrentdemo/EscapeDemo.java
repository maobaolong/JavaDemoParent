package net.mbl.demo.concurrentdemo;

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
    return mValue;
  }
  public static void main(String[] args) {
    EscapeDemo escapeDemo = new EscapeDemo();
    System.out.println("main exit!");
  }
  
  class InnerClass{
    public InnerClass(EscapeDemo escapeDemo) {
      System.out.println(escapeDemo.getValue());
    }
  }
}
