package net.mbl.demo;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;
import org.apache.commons.io.FilenameUtils;

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
        Options options = new Options();
        options.setCreateIfMissing(true);
        rocksDB = RocksDB.open(options, path);
        RocksIterator iter = rocksDB.newIterator();
        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println("iter key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
        }

    }
}
