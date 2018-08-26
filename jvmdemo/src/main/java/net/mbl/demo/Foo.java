package net.mbl.demo;

/**
 * Created by mbl on 15/06/2018.
 */
public class Foo {
  int age = 3;
  int v1=2,v2=4,v3=5,v4=6,v5=7,v6=8,v7=9,v8=10;
  Foo2 foo2 = new Foo2(3);
  Foo2 foo22 = new Foo2(3);
  Foo(int age) {
    this.age = 3;

  }
  public void aaa () {
    synchronized (this) {
      System.out.println("aa");
    }
  }

  public void bbb () {
    synchronized (this) {
      System.out.println("bb");
    }
  }

  public int getAge() {
    return age;
  }
}
