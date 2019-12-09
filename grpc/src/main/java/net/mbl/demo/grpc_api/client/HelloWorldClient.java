package net.mbl.demo.grpc_api.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.mbl.demo.grpc_api.helloworld.GreeterGrpc;
import net.mbl.demo.grpc_api.helloworld.HelloWorldProto.HelloReply;
import net.mbl.demo.grpc_api.helloworld.HelloWorldProto.HelloRequest;
import java.util.concurrent.TimeUnit;

public class HelloWorldClient {

  private final ManagedChannel channel;
  private final GreeterGrpc.GreeterBlockingStub blockingStub;


  public HelloWorldClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build();

    blockingStub = GreeterGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void greet(String name) {
    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response = blockingStub.sayHello(request);
    System.out.println(response.getMessage());

  }

  public static void main(String[] args) throws InterruptedException {
    HelloWorldClient client = new HelloWorldClient("127.0.0.1", 50051);
    for (int i = 0; i < 5; i++) {
      client.greet("world:" + i);
    }
  }
}
