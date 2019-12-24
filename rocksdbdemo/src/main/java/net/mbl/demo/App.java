package net.mbl.demo;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.hdds.conf.OzoneConfiguration;
import org.apache.hadoop.hdds.utils.db.DBStore;
import org.apache.hadoop.hdds.utils.db.DBStoreBuilder;
import org.apache.hadoop.hdds.utils.db.RocksDBConfiguration;
import org.apache.hadoop.hdds.utils.db.Table;
import org.apache.hadoop.hdds.utils.db.TableIterator;
import org.rocksdb.RocksDB;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Paths;



/**
 * Hello world!
 */
public class App {
    static {
        RocksDB.loadLibrary();
    }

    static RocksDB rocksDB;
    static String path = FilenameUtils.concat("/tmp/rocksdb-dirs", "testdb");
    ;

    public static void main(String[] args) throws Exception {
//        Options options = new Options();
//        options.setCreateIfMissing(true);
//        rocksDB = RocksDB.open(options, path);
//        RocksIterator iter = rocksDB.newIterator();
//        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
//            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
//        }

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
        TableIterator<String, ? extends Table.KeyValue<String, String>> it = table.iterator();
        for (it.seekToFirst(); it.hasNext(); it.next()) {
            System.out.println("iter key:" + it.key() + ", iter value:" + it.value().getValue());
        }
    }
}
