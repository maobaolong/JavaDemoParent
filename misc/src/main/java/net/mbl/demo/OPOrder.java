package net.mbl.demo;

public class OPOrder {

  static boolean c1() {
    System.out.println("c1");
    return false;
  }

  static boolean c2() {
    System.out.println("c2");
    return true;
  }

  static boolean c3() {
    System.out.println("c3");
    return false;
  }

  public static void main(String[] args) {
    System.out.println(c1() || c2() && c3());
  }
}
