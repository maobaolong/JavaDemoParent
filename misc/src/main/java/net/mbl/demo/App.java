package net.mbl.demo;

import com.sun.tools.javah.Util;
import org.apache.hadoop.util.ExitUtil;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.*;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.apache.hadoop.util.ExitUtil.terminate;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Logger LOG = LoggerFactory.getLogger("testApp");

    private static String byte2Str(byte[] bytes) {
        String str = "";
        for (byte b : bytes) {
            str += (0xff & b) + " ";
        }
        LOG.info("i am here", new Throwable("i am exception"));
        return str;
    }
    public static void test () {
        try {
            Object o = null;
            o.toString();
        } finally {
            System.out.println("finally");
        }
    }

    public static void test2() throws IOException {
        try {
            throw new IOException("");
        } catch (Exception e) {
            throw e;
        }
        finally {
            System.out.println("finally");
        }
    }
    public static void main( String[] args ) throws IOException, InterruptedException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder("http://bdp.jd.com/api/urm/sms/send.ajax?appCode=bdpeye.jd.com&token=OR1r7ZRV85A@%26SoygtRw%26dMxFF%5Eu0%2A$W&erp=maobaolong&content=aaabbbcccddd");
        Thread.sleep(0);
//        Thread.sleep(-2);
        try {
            test();
        } catch (NullPointerException npe) {
            System.out.println("hrer");
            throw npe;
        } finally {
            System.out.println("finally2");
        }
        Date d = new Date();
        Thread.sleep(1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(1020);
        System.out.println("耗时：" + sdf.format(c.getTime()));
        File f = new File("/tmp/b/c");
        File[] children = f.listFiles();
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(f));
            ps.println("http://www.jb51.net");// 往文件里写入字符串
            ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String aa = null;
        System.out.println(aa instanceof String);
        System.out.println( "Hello World!" );
        String[] textSuite = new String[]{
            "0.0.0.0",
            "192.168.100.121",
            "172.168.100.121",
            "172.168.100",
            "172.168",
            "172",
            "",
            " ",
            "0",
            "W",
        };
        for (int i = 0; i < textSuite.length; i++) {
            byte[] ipByte = IPAddressUtil.textToNumericFormatV4(textSuite[i]);
            System.out.print(i + " : ");
            try {
                System.out.println(byte2Str(ipByte));
            } catch(Exception e) {
//                e.printStackTrace();
//                e.printStackTrace(System.out);
                LOG.warn("warn" ,e);
            }
        }
        try {
            try {
                Object o = null;
                o.toString();
            } catch (Exception e) {
                System.out.println("exception");
                terminate(1);
            } finally {
                System.out.println("final");
            }
        } catch (ExitUtil.ExitException ee) {
            ee.printStackTrace();
        }
    }
}
