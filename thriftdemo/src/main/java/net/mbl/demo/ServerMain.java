package net.mbl.demo;

import net.mbl.demo.thrift.HelloWorldService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransportException;

/**
 * Hello world!
 *
 */
public class ServerMain
{
    public static void main( String[] args ) throws TTransportException {
        test3();
    }
    static void test1() throws TTransportException {
        TProcessor processor = new HelloWorldService.Processor<HelloWorldService.Iface>(new HelloWorldServiceImpl());

        TServerSocket serverTransport = new TServerSocket(7912);
        //设置协议工厂为TBinaryProtocol.Factory
        Factory proFactory = new TBinaryProtocol.Factory();
        TServer.Args tArgs = new TServer.Args(serverTransport);
        tArgs.processor(processor);
        tArgs.protocolFactory(proFactory);
        //使用TSimpleServer
        TServer server = new TSimpleServer(tArgs);
        System.out.println("Start server on port 7912....");
        server.serve();
        System.out.println("done.");
    }

    static void test2() throws TTransportException {

        TProcessor processor =   new   HelloWorldService.Processor<HelloWorldService.Iface>(new   HelloWorldServiceImpl());

        TNonblockingServerTransport serverTransport =   new TNonblockingServerSocket(7912);

        Factory proFactory =   new   TBinaryProtocol.Factory();
        THsHaServer.Args rpcArgs =   new THsHaServer.Args(serverTransport);
        rpcArgs.processor(processor);
        rpcArgs.protocolFactory(proFactory);

        TServer server =   new THsHaServer(rpcArgs);
        System.out.println("Start server on port 7912....");
        server.serve();
        System.out.println("done.");
    }
    static void test3() throws TTransportException {
        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        processor.registerProcessor(Constants.HELLO_CLIENT_SERVICE_NAME, new   HelloWorldService.Processor<HelloWorldService.Iface>(new   HelloWorldServiceImpl()));

        TNonblockingServerTransport serverTransport =   new TNonblockingServerSocket(7912);

        Factory proFactory =   new   TBinaryProtocol.Factory();
        THsHaServer.Args rpcArgs =   new THsHaServer.Args(serverTransport);
        rpcArgs.processor(processor);
        rpcArgs.protocolFactory(proFactory);

        TServer server =   new THsHaServer(rpcArgs);
        System.out.println("Start server on port 7912....");
        server.serve();
        System.out.println("done.");
    }
}
