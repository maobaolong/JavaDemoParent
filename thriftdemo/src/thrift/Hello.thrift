namespace java net.mbl.demo.thrift

service  HelloWorldService {
  string sayHello(1:string username)
}