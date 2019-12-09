package net.mbl.demo.grpc_api.client;

import java.util.concurrent.TimeUnit;
import net.mbl.demo.grpc_api.GrpcServiceGrpc;
import net.mbl.demo.grpc_api.Grpc.UnaryRequest;
import net.mbl.demo.grpc_api.Grpc.UnaryResponse;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

//grpc客户端类
public class GrpcClient {

  private final ManagedChannel channel;//客户端与服务器的通信channel
  private final GrpcServiceGrpc.GrpcServiceBlockingStub blockStub;//阻塞式客户端存根节点

  public GrpcClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true)
        .build();//指定grpc服务器地址和端口初始化通信channel
    blockStub = GrpcServiceGrpc.newBlockingStub(channel);//根据通信channel初始化客户端存根节点
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  //客户端方法
  public void sayHello(String str) {
    //封装请求参数
    UnaryRequest request = UnaryRequest.newBuilder().setServiceName("GrpcServiceRequest")
        .setMethodName("sendUnaryRequest").setData(ByteString.copyFrom(str.getBytes()))
        .build();
    //客户端存根节点调用grpc服务接口，传递请求参数
    UnaryResponse response = blockStub.sendUnaryRequest(request);
    System.out.println(
        "client, serviceName:" + response.getServiceName() + "; methodName:" + response
            .getMethodName());
  }

  public static void main(String[] args) throws InterruptedException {
    //初始化grpc客户端对象
    GrpcClient client = new GrpcClient("127.0.0.1", 50051);
    for (int i = 0; i < 5; i++) {
      client.sayHello("client word:" + i);
      Thread.sleep(3000);
    }
  }
}