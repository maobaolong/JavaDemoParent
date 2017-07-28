package net.mbl.demo;

/**
 * Created by mbl on 2017/7/6.
 */
public class TryCatchFinally {
  private static int func() {
    try{
      System.out.println(0);
      TryCatchFinally d = null;
      d.toString();
      System.out.println(1);
      return 1;
    } catch (Exception e) {
      System.out.println(2);
      return 2;
    } finally {
      System.out.println(3);
      //return 3;
    }
  }
  public static void main(String[] args) {
    System.out.println("ret = " + func());
  }
}
