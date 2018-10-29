package net.mbl.demo;

public class StaticBlock {

  public static void main(String[] args) {
    System.out.print(B.c);
  }
}

class A {
  static {
    System.out.print("A");
  }
}

class B extends A{
  static {
    System.out.print("B");
  }
  public final static String c = "C";
}