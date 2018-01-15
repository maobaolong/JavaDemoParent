package net.mbl.demo;

/**
 * Created by mbl on 23/11/2017.
 */
public class MapTest {
  private static final int MAXIMUM_CAPACITY = 1 << 30;
  public static void main(String[] args) {
    for (int i=65;i<66;i++) {
      int result = tableSizeFor(i);
      System.out.println("i=" + i + ", result = " + result);
    }

  }

  private static final int tableSizeFor(int c) {
    int n = c - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }
}
