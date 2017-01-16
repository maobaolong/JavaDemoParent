namespace java net.mbl.demo.thrift

exception MyTException {
  1: string type // deprecated since 1.1 and will be removed in 2.0
  2: string message
  3: string className
}

exception ThriftIOException {
  1: string message
}

