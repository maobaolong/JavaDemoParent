package net.mbl.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mbl on 06/12/2017.
 */
public class CollectionTest {
  public static void main(String[] args){
    System.out.println("h");
    List<String> srclst = new LinkedList<>();


    // populate two lists
    srclst.add("Java");
    srclst.add("is");
    srclst.add("best");

//    destlst.add("C++");
//    destlst.add("is");
//    destlst.add("older");
//    destlst.add("ss");
    List<String> destlst = new LinkedList<>(srclst);
    srclst.set(1, "is not");
    // copy into dest list
//    Collections.copy(destlst, srclst);

    System.out.println("Value of source list: "+srclst);
    System.out.println("Value of destination list: "+destlst);
  }
}
