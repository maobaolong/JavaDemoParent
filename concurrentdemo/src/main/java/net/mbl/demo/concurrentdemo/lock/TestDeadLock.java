package net.mbl.demo.concurrentdemo.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReentrantLock;  
  
/** 
 * Created by vincent on 2015/9/3. 
 */  
public class TestDeadLock {  
    private static Lock lockA = new ReentrantLock();  
    private static Lock lockB = new ReentrantLock();  
  
    public static void main(String args[]){  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
                try{  
                    lockA.lock();  
                    TimeUnit.SECONDS.sleep(2);  
                    try{  
                        lockB.lock();  
                    }finally {  
                        lockB.unlock();  
                    }  
                }catch (InterruptedException e) {  
  
                }finally {  
                    lockA.unlock();  
                }  
            }  
        }).start();  
        new Thread(new Runnable() {  
            @Override  
            public void run() {  
                try{  
                    lockB.lock();  
                    TimeUnit.SECONDS.sleep(2);  
                    try{  
                        lockA.lock();  
                    }finally {  
                        lockA.unlock();  
                    }  
                }catch (InterruptedException e) {  
  
                }finally {  
                    lockB.unlock();  
                }  
            }  
        }).start();  
    }  
} 