package net.mbl.demo;

public class SInstance {
  private static SInstance INSTANCE = null;
  private SInstance() {

  }
  public static SInstance getINSTANCE() {
    if(INSTANCE==null){
     synchronized (SInstance.class){
       if(INSTANCE==null){
         INSTANCE = new SInstance();
       }
     }
    }
    return INSTANCE;
  }
}
