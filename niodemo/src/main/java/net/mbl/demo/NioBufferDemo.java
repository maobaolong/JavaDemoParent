package net.mbl.demo;

import net.mbl.demo.common.FormatUtils;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Hello world!
 *
 */
public class NioBufferDemo
{
    //-Xms256m -Xmx512m -XX:+UseG1GC -XX:G1HeapRegionSize=1m -XX:+UnlockExperimentalVMOptions   -XX:+UnlockDiagnosticVMOptions  -XX:+PrintFlagsFinal  -Xloggc:/tmp/gc.log -verbose:gc -XX:+G1PrintRegionLivenessInfo -XX:+PrintGCCause -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime  -XX:+PrintTenuringDistribution -XX:PrintFLSStatistics=2 -XX:+PrintAdaptiveSizePolicy
    // -XX:+PrintClassHistogramAfterFullGC -XX:+PrintClassHistogramBeforeFullGC -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1
    public static void main( String[] args )
    {
//        heapByteBufferDemo();
        directByteBufferDemo();
    }

    static void directByteBufferDemo() {
        // directMemory如果分配可能会满，则先调用system.gc()请求fullgc。
        // 如果有jvm参数"-XX:+DisableExplicitGC",则system.gc()会被忽略，fullgc失效。导致
        // "Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory"
        System.out.println("maxDirectMemory = " + VM.maxDirectMemory()
            + ", isDirectMemoryPageAligned = " + VM.isDirectMemoryPageAligned());
        ByteBuffer bb0 = ByteBuffer.allocate(100*1024*1024);
        bb0 = null;
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
        DirectBuffer bb = (DirectBuffer) ByteBuffer.allocateDirect(500*1024*1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
        bb = null;
        bb = (DirectBuffer) ByteBuffer.allocateDirect(500*1024*1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
        // 显示清理直接内存
        bb.cleaner().clean();
        System.out.println("after clean:" + SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
        bb = (DirectBuffer) ByteBuffer.allocateDirect(500*1024*1024);
        System.out.println(SharedSecrets.getJavaNioAccess().getDirectBufferPool().getMemoryUsed());
        System.out.println("done!");
    }

    static void heapByteBufferDemo() {
        System.out.println( "Hello World!" );
        System.out.println("maxMemory="
            + FormatUtils.getSizeFromBytes(Runtime.getRuntime().maxMemory()));
        System.out.println("----------Test allocate--------");
        System.out.println("before alocate: free=" + Runtime.getRuntime().freeMemory()
            + ", total=" + Runtime.getRuntime().totalMemory());

        // 如果分配的内存过小，调用Runtime.getRuntime().freeMemory()大小不会变化？
        // 要超过多少内存大小JVM才能感觉到？
        ByteBuffer buffer = ByteBuffer.allocate(128*1024*1024);
        System.out.println("buffer = " + buffer);

        System.out.println("after alocate: free=" + Runtime.getRuntime().freeMemory()
            + ", total=" + Runtime.getRuntime().totalMemory());

        // 这部分直接用的系统内存，所以对JVM的内存没有影响
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(102400);
        System.out.println("directBuffer = " + directBuffer);
        System.out.println("after direct alocate: free=" + Runtime.getRuntime().freeMemory()
            + ", total=" + Runtime.getRuntime().totalMemory());

        System.out.println("----------Test wrap--------");
        byte[] bytes = new byte[32];
        buffer = ByteBuffer.wrap(bytes);
        System.out.println(buffer);

        buffer = ByteBuffer.wrap(bytes, 10, 10);
        System.out.println(buffer);

        System.out.println("--------Test reset----------");
        buffer.clear();
        buffer.position(5);
        buffer.mark();
        buffer.position(10);
        System.out.println("before reset:" + buffer);
        buffer.reset();
        System.out.println("after reset:" + buffer);

        System.out.println("--------Test rewind--------");
        buffer.clear();
        buffer.position(10);
        buffer.limit(15);
        System.out.println("before rewind:" + buffer);
        buffer.rewind();
        System.out.println("before rewind:" + buffer);

        System.out.println("--------Test compact--------");
        buffer.clear();
        buffer.put("abcd".getBytes());
        System.out.println("before compact:" + buffer);
        System.out.println(new String(buffer.array()));
        buffer.flip();
        System.out.println("after flip:" + buffer);
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println("after three gets:" + buffer);
        System.out.println("\t" + new String(buffer.array()));
        buffer.compact();
        System.out.println("after compact:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("------Test get-------------");
        buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
            .put((byte) 'e').put((byte) 'f');
        System.out.println("before flip()" + buffer);
        // 转换为读取模式
        buffer.flip();
        System.out.println("before get():" + buffer);
        System.out.println((char) buffer.get());
        System.out.println("after get():" + buffer);
        // get(index)不影响position的值
        System.out.println((char) buffer.get(2));
        System.out.println("after get(index):" + buffer);
        byte[] dst = new byte[10];
        buffer.get(dst, 0, 2);
        System.out.println("after get(dst, 0, 2):" + buffer);
        System.out.println("\t dst:" + new String(dst));
        System.out.println("buffer now is:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("--------Test put-------");
        ByteBuffer bb = ByteBuffer.allocate(32);
        System.out.println("before put(byte):" + bb);
        System.out.println("after put(byte):" + bb.put((byte) 'z'));
        System.out.println("\t" + bb.put(2, (byte) 'c'));
        // put(2,(byte) 'c')不改变position的位置
        System.out.println("after put(2,(byte) 'c'):" + bb);
        System.out.println("\t" + new String(bb.array()));
        // 这里的buffer是 abcdef[pos=3 lim=6 cap=32]
        bb.put(buffer);
        System.out.println("after put(buffer):" + bb);
        System.out.println("\t" + new String(bb.array()));
    }
}
