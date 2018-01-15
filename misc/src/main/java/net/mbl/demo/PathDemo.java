package net.mbl.demo;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by mbl on 09/11/2017.
 */
public class PathDemo {
  public static void main(String[] args) {
    Path p = Paths.get("/a/b/c/d/e");
    System.out.println(p);
    System.out.println(p.getNameCount());
    System.out.println(p.getParent());
    System.out.println(p.getRoot());
    System.out.println(p.getRoot() + p.subpath(0,3).toString());
    System.out.println(p.subpath(0,2));
    System.out.println(p.getRoot().resolve(p.subpath(0,3)));
    Path tmpP = p;
    while ((tmpP = tmpP.getParent()).getNameCount() > 1) {
      System.out.println(tmpP);
    }
  }
}