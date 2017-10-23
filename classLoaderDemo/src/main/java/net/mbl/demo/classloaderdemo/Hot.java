package net.mbl.demo.classloaderdemo;

public class Hot {
    public void hot() {
        System.out.println(" version 2 : " + this.getClass().getClassLoader());
    }
}