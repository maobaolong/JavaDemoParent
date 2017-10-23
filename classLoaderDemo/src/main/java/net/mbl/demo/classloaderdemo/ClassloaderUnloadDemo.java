package net.mbl.demo.classloaderdemo;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

/**
 * Created by mbl on 23/10/2017.
 */
public class ClassloaderUnloadDemo {
  String className = "net.mbl.demo.classloaderdemo.Hot";
  Class hotClazz = null;
  HotSwapURLClassLoader hotSwapCL = null;
  //-verbose:class
  public static void main(String[] args) throws Exception {
    //获取有关类型加载的JMX接口
    ClassLoadingMXBean loadingBean = ManagementFactory.getClassLoadingMXBean();

    ClassloaderUnloadDemo classloaderUnloadDemo = new ClassloaderUnloadDemo();
    classloaderUnloadDemo.initLoad();
    Object hot = classloaderUnloadDemo.hotClazz.newInstance();
    Method m = classloaderUnloadDemo.hotClazz.getMethod("hot");
    m.invoke(hot, null); //打印出相关信息
    System.out.println("total: " + loadingBean.getTotalLoadedClassCount());
    System.out.println("active: " + loadingBean.getLoadedClassCount());
    System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());
    System.gc();
    System.out.println("total: " + loadingBean.getTotalLoadedClassCount());
    System.out.println("active: " + loadingBean.getLoadedClassCount());
    System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());
    hot = null;
    classloaderUnloadDemo.hotClazz = null;
    classloaderUnloadDemo.hotSwapCL = null;
    classloaderUnloadDemo = null;
    // 执行一次gc垃圾回收
    System.gc();
    System.out.println("GC over");
    System.out.println("total: " + loadingBean.getTotalLoadedClassCount());
    System.out.println("active: " + loadingBean.getLoadedClassCount());
    System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());
  }

    /**
     * 加载class
     */
  public void initLoad() throws Exception {
    hotSwapCL = HotSwapURLClassLoader.getClassLoader();
    // 如果Hot类被修改了，那么会重新加载，hotClass也会返回新的
    hotClazz = hotSwapCL.loadClass(className);
  }
}
