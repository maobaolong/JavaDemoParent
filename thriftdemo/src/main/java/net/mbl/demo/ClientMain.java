package net.mbl.demo;

import net.mbl.demo.authentication.TransportProvider;
import net.mbl.demo.client.HelloClient;
import net.mbl.demo.exception.MyException;
import net.mbl.demo.thrift.HelloWorldService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ClientMain {
    /**
     * 调用Hello服务
     * @param args
     */
    public static void main(String[] args) throws IOException {

        test0();
    }
    static void test0(){
        InetSocketAddress address = new InetSocketAddress("localhost", 7912);
        HelloClient helloClient = new HelloClient(address);
        try {
            String ret = helloClient.sayHello("mbl");
            System.out.println(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }
    static void test1() throws IOException {
        try {
            //设置调用的服务器为本地，端口为7911
            TTransport transport = new TSocket("localhost", 7912);

            //设置传输协议为TBinaryProtocol
//            TProtocol protocol = new TBinaryProtocol(transport);

            InetSocketAddress mAddress = new InetSocketAddress("localhost", 7912);
            TransportProvider mTransportProvider = TransportProvider.Factory.create();
            TProtocol protocol =
                    new TBinaryProtocol(mTransportProvider.getClientTransport(null, mAddress));
            protocol = new TMultiplexedProtocol(protocol, Constants.HELLO_CLIENT_SERVICE_NAME);


            protocol.getTransport().open();
            HelloWorldService.Client client = new HelloWorldService.Client(protocol);
            System.out.println(client.sayHello("mbl"));
            protocol.getTransport().close();


        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static void test2() throws IOException {
        try {
            //设置调用的服务器为本地，端口为7911
            TTransport transport = new TSocket("localhost", 7912);
            transport = new TFramedTransport(transport, 16384000);
            //设置传输协议为TBinaryProtocol
            TProtocol protocol = new TBinaryProtocol(transport);


            protocol.getTransport().open();
            HelloWorldService.Client client = new HelloWorldService.Client(protocol);
            System.out.println(client.sayHello("mbl"));
            protocol.getTransport().close();


        } catch (TTransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}