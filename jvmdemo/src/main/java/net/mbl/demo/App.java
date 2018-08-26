package net.mbl.demo;

import net.mbl.demo.common.ObjectSizer;

/**
 * Hello world!
 *
 */
public class App 
{
    static Object rel;
    public static void main( String[] args )
    {
        test();
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static int test() {
        Foo f = new Foo(3);
//        ObjectSizer objectSizer = new ObjectSizer();
//        long used = objectSizer.getMemoryUse();
        f = new Foo(4);
//        long diff = objectSizer.getMemoryUse() - used;
//        System.out.println(diff);
        return f.getAge();
    }
}
