package net.mbl.demo;

import net.mbl.demo.thrift.MyTException;
import net.mbl.demo.thrift.HelloWorldService;
import net.mbl.demo.thrift.ThriftIOException;
import net.mbl.demo.util.RpcUtils;

import java.io.IOException;

public class HelloWorldServiceImpl implements HelloWorldService.Iface {

    public String sayHello(final String s) throws ThriftIOException, MyTException {
        return RpcUtils.call(new RpcUtils.RpcCallableThrowsIOException<String>() {
            @Override
            public String call() throws IOException {
                return "hello" + s;
            }
        });

    }
}