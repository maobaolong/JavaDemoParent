package net.mbl.demo.concurrentdemo.lock.demo2;

public interface IBuffer {
    public void write();  
    public void read() throws InterruptedException;  
}  