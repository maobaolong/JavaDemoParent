package net.mbl.demo.gcdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbl on 18/10/2017.
 */
public final class GcDemo {
  private final static int BYTE_SIZE = 1 * 1024 * 1024;
// -XX:+UseG1GC  -XX:G1HeapRegionSize=4m -XX:MaxGCPauseMillis=200  -Xmn40m -Xms100m -Xmx100m     -XX:MaxTenuringThreshold=5 -server -XX:+UnlockExperimentalVMOptions   -XX:+UnlockDiagnosticVMOptions  -XX:+PrintFlagsFinal -Xloggc:/tmp/gc_demo.log -verbose:gc -XX:+PrintGCCause -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintTenuringDistribution -XX:PrintFLSStatistics=2 -XX:+PrintAdaptiveSizePolicy
// -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled  -Xmn40m -Xms100m -Xmx100m   -XX:MaxTenuringThreshold=5 -server -XX:+UnlockExperimentalVMOptions   -XX:+UnlockDiagnosticVMOptions  -XX:+PrintFlagsFinal -Xloggc:/tmp/gc_demo.log -verbose:gc -XX:+PrintGCCause -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintTenuringDistribution -XX:PrintFLSStatistics=2 -XX:+PrintAdaptiveSizePolicy
  public static void main(String []args) throws InterruptedException {
    List<Object> list = new ArrayList<Object>();
    List<Object> list2 = new ArrayList<Object>();

    byte[] actor = new byte[BYTE_SIZE];
    for (int j = 0; j < 10; j++) {
      for (int i = 0; i < 10; i++) {
        list.add(new byte[BYTE_SIZE]);
        Thread.sleep(1000);
        System.out.println(i);
      }
      list.clear();
    }
    for(int i = 0 ; i < 20 ; i ++) {
      list2.add(new byte[BYTE_SIZE]);
      Thread.sleep(1000);
      System.out.println(i);
    }
    list.clear();
    for(int i = 0 ; i < 80 ; i ++) {
      list2.add(new byte[BYTE_SIZE]);
      Thread.sleep(1000);
      System.out.println(i);
    }
  }
}
