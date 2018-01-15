package net.mbl.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

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
        return str;
    }
    public static void main( String[] args )
    {
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
    }
}
