package net.mbl.demo;

import alluxio.AlluxioURI;
import alluxio.client.keyvalue.KeyValueStoreReader;
import alluxio.client.keyvalue.KeyValueStoreWriter;
import alluxio.client.keyvalue.KeyValueSystem;

import java.util.Properties;


public class AlluxioTest {
    public static  void  main(String[] args) throws Exception{
        System.setProperty("alluxio.master.hostname", "172.16.185.35");
        KeyValueSystem kvs = KeyValueSystem.Factory.create();
        // socks代理服务器的地址与端口, 或者打开代理软件，测试需要，线上不需要
        Properties prop = System.getProperties();
//        prop.setProperty("socksProxyHost", "172.22.91.34");
//        prop.setProperty("socksProxyPort", "80");

//        System.setProperty("http.proxyHost", "172.22.91.78");
//        System.setProperty("http.proxyPort", "80");

        KeyValueStoreWriter writer = kvs.createStore(new AlluxioURI("/path/my-kvstore/"));
        // Insert key-value pair ("100", "foo")
        writer.put("100".getBytes(), "foo".getBytes());
        // Close and complete the store
        writer.close();
        System.out.println("ceate ok!");
        KeyValueStoreReader reader = kvs.openStore(new AlluxioURI("/path/my-kvstore/"));
        // Return "foo"
        System.out.println(String.valueOf(reader.get("100".getBytes())));
        // Return null as no value associated with "300"
        System.out.println(reader.get("300".getBytes()));
        // Close the reader on the store
        reader.close();
    }

}
