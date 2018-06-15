package net.mbl.demo;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    Map<String, String> map = Maps.newHashMap();
    map.put("nn1", "nn1");
    map.put("nn2", "nn2");
    for (String v : map.values()) {
      System.out.println(v);
    }

    map = Maps.newHashMap();
    map.put("nn4", "nn4");
    map.put("nn3", "nn3");
    for (String v : map.values()) {
      System.out.println(v);
    }
    map = Maps.newHashMap();
    map.put("nn5", "nn5");
    map.put("nn6", "nn6");
    for (String v : map.values()) {
      System.out.println(v);
    }

    map = Maps.newHashMap();
    map.put("nn11", "nn11");
    map.put("nn12", "nn12");
    for (String v : map.values()) {
      System.out.println(v);
    }

    map = Maps.newHashMap();
    map.put("r1", "r1");
    map.put("r2", "r2");
    for (String v : map.values()) {
      System.out.println(v);
    }
  }
}
