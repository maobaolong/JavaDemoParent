package net.mbl.demo;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hdds.conf.OzoneConfiguration;
import org.apache.hadoop.hdds.utils.db.DBStore;
import org.apache.hadoop.hdds.utils.db.DBStoreBuilder;
import org.apache.hadoop.hdds.utils.db.RocksDBConfiguration;
import org.apache.hadoop.hdds.utils.db.Table;
import org.apache.hadoop.hdds.utils.db.TableIterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        OzoneConfiguration ozoneConfiguration = new OzoneConfiguration();
        RocksDBConfiguration rocksDBConfiguration =
                ozoneConfiguration.getObject(RocksDBConfiguration.class);
        DBStoreBuilder builder = DBStoreBuilder.newBuilder(ozoneConfiguration, rocksDBConfiguration);
        builder.setPath(Paths.get("/tmp/dbPath")).setName("testdb").addTable("test");
        FileUtils.forceMkdir(new File("/tmp/dbPath"));
        DBStore dbStore = builder.build();
        Table<String, String> table = dbStore.getTable("test", String.class, String.class);
        table.put("a", "1");
        table.put("b", "2");
        table.put("b", "3");
        table.put("c", "4");
        table.delete("a");
        ThreadPoolExecutor tp = new ThreadPoolExecutor(100, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        IntStream.range(0, 100).forEach(i -> tp.submit(() -> {
            try {
                table.put(String.valueOf(i), "gaga");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        System.out.println("ok");
        TableIterator<String, ? extends Table.KeyValue<String, String>> it = table.iterator();
        for (it.seekToFirst(); it.hasNext(); it.next()) {
            System.out.println("iter key:" + it.key() + ", iter value:" + it.value().getValue());
        }
        System.out.println(table.getEstimatedKeyCount());
    }
}
