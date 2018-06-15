package net.mbl.demo;

import com.sun.tools.javac.code.Source;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by mbl on 09/05/2018.
 */
public class DyProxy {
  static int add(int a) {
    return add(++a);

  }
  public static void main(String[] args) {
    add(1);
  }
}
