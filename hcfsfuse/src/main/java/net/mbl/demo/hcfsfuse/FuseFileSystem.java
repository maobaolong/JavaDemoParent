package net.mbl.demo.hcfsfuse;

import alluxio.collections.IndexDefinition;
import alluxio.collections.IndexedSet;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jnr.ffi.Pointer;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import ru.serce.jnrfuse.ErrorCodes;
import ru.serce.jnrfuse.FuseFillDir;
import ru.serce.jnrfuse.FuseStubFS;
import ru.serce.jnrfuse.struct.FileStat;
import ru.serce.jnrfuse.struct.FuseFileInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class FuseFileSystem extends FuseStubFS {
    private static final int MAX_OPEN_FILES = Integer.MAX_VALUE;
    private static final int MAX_OPEN_WAITTIME_MS = 5000;

    private final Path rootPath;
    private final FileSystem fs;
    // Table of open files with corresponding InputStreams and OutputStreams
    private final IndexedSet<OpenFileEntry> mOpenFiles;
    private AtomicLong mNextOpenFileId = new AtomicLong(0);
    private final LoadingCache<String, Path> mPathResolverCache;

    // Open file managements
    private static final IndexDefinition<OpenFileEntry, Long> ID_INDEX =
        new IndexDefinition<OpenFileEntry, Long>(true) {
            @Override
            public Long getFieldValue(OpenFileEntry o) {
                return o.getId();
            }
        };

    private static final IndexDefinition<OpenFileEntry, String> PATH_INDEX =
        new IndexDefinition<OpenFileEntry, String>(true) {
            @Override
            public String getFieldValue(OpenFileEntry o) {
                return o.getPath();
            }
        };

    public FuseFileSystem(Configuration conf, FuseOptions fuseOptions) throws IOException {
        fuseOptions.getMountPoint();
        this.rootPath = new Path(fuseOptions.getRoot());
        this.fs = rootPath.getFileSystem(conf);
        mPathResolverCache = CacheBuilder.newBuilder()
                .maximumSize(500)
                .build(new PathCacheLoader());
        mOpenFiles = new IndexedSet<>(ID_INDEX, PATH_INDEX);
    }

    @Override
    public int getattr(String path, FileStat stat) {
        int res = 0;
        final Path turi = mPathResolverCache.getUnchecked(path);
        try {
            FileStatus status = fs.getFileStatus(turi);
            stat.st_size.set(status.getLen());
            int mode = 00777;
            if (status.isDirectory()) {
                mode |= FileStat.S_IFDIR;
            } else {
                mode |= FileStat.S_IFREG;
            }
            stat.st_mode.set(mode);
        } catch (IOException e) {
            log.debug("Failed to get info of {}, path does not exist or is invalid", path);
            return -ErrorCodes.ENOENT();
        }

        return res;
    }

    /**
     * Reads the contents of a directory.
     *
     * @param path   The FS path of the directory
     * @param buff   The FUSE buffer to fill
     * @param filter FUSE filter
     * @param offset Ignored in fuse
     * @param fi     FileInfo data structure kept by FUSE
     * @return 0 on success, a negative value on error
     */
    @Override
    public int readdir(String path, Pointer buff, FuseFillDir filter,
            @off_t long offset, FuseFileInfo fi) {
        final Path turi = mPathResolverCache.getUnchecked(path);
        log.trace("readdir({}) [target: {}]", path, turi);

        try {
            final FileStatus[] ls = fs.listStatus(turi);
            // standard . and .. entries
            filter.apply(buff, ".", null, 0);
            filter.apply(buff, "..", null, 0);

            for (FileStatus file : ls) {
                filter.apply(buff, file.getPath().getName(), null, 0);
            }
        } catch (FileNotFoundException | InvalidPathException e) {
            log.debug("Failed to read directory {}, path does not exist or is invalid", path);
            return -ErrorCodes.ENOENT();
        } catch (Throwable t) {
            log.error("Failed to read directory {}", path, t);
            return -1;
        }

        return 0;
    }

    @Override
    public int open(String path, FuseFileInfo fi) {
        final Path turi = mPathResolverCache.getUnchecked(path);
        // (see {@code man 2 open} for the structure of the flags bitfield)
        // File creation flags are the last two bits of flags
        final int flags = fi.flags.get();
        log.trace("open({}, 0x{}) [target: {}]", path, Integer.toHexString(flags), turi);
        if (mOpenFiles.size() >= MAX_OPEN_FILES) {
            log.error("Cannot open {}: too many open files (MAX_OPEN_FILES: {})", path, MAX_OPEN_FILES);
            return ErrorCodes.EMFILE();
        }
        FSDataInputStream is;
        try {
            is = fs.open(turi);
        } catch (Throwable t) {
            log.error("Failed to open file {}", path, t);
            if (t instanceof IOException) {
                return -ErrorCodes.EIO();
            } else {
                return -ErrorCodes.EBADMSG();
            }
        }
        long fid = mNextOpenFileId.getAndIncrement();
        mOpenFiles.add(new OpenFileEntry(fid, path, is, null));
        fi.fh.set(fid);

        return 0;
    }

    /**
     * Reads data from an open file.
     *
     * @param path the FS path of the file to read
     * @param buf FUSE buffer to fill with data read
     * @param size how many bytes to read. The maximum value that is accepted
     *             on this method is {@link Integer#MAX_VALUE} (note that current
     *             FUSE implementation will call this method with a size of
     *             at most 128K).
     * @param offset offset of the read operation
     * @param fi FileInfo data structure kept by FUSE
     * @return the number of bytes read or 0 on EOF. A negative
     *         value on error
     */
    @Override
    public int read(String path, Pointer buf, @size_t long size, @off_t long offset,
            FuseFileInfo fi) {

        if (size > Integer.MAX_VALUE) {
            log.error("Cannot read more than Integer.MAX_VALUE");
            return -ErrorCodes.EINVAL();
        }
        log.trace("read({}, {}, {})", path, size, offset);
        final int sz = (int) size;
        final long fd = fi.fh.get();
        OpenFileEntry oe = mOpenFiles.getFirstByField(ID_INDEX, fd);
        if (oe == null) {
            log.error("Cannot find fd for {} in table", path);
            return -ErrorCodes.EBADFD();
        }

        int rd = 0;
        int nread = 0;
        if (oe.getIn() == null) {
            log.error("{} was not open for reading", path);
            return -ErrorCodes.EBADFD();
        }
        try {
            oe.getIn().seek(offset);
            final byte[] dest = new byte[sz];
            while (rd >= 0 && nread < size) {
                rd = oe.getIn().read(dest, nread, sz - nread);
                if (rd >= 0) {
                    nread += rd;
                }
            }

            // EOF
            if (nread == -1) {
                nread = 0;
            } else if (nread > 0) {
                buf.put(0, dest, 0, nread);
            }
        } catch (Throwable t) {
            log.error("Failed to read file {}", path, t);
            if (t instanceof IOException) {
                return -ErrorCodes.EIO();
            } else {
                return -ErrorCodes.EBADMSG();
            }
        }

        return nread;
    }

    /**
     * Flushes cached data on target.
     * <p>
     * Called on explicit sync() operation or at close().
     *
     * @param path The path on the FS of the file to close
     * @param fi   FileInfo data struct kept by FUSE
     * @return 0 on success, a negative value on error
     */
    @Override
    public int flush(String path, FuseFileInfo fi) {
        log.trace("flush({})", path);
        final long fd = fi.fh.get();
        OpenFileEntry oe = mOpenFiles.getFirstByField(ID_INDEX, fd);
        if (oe == null) {
            log.error("Cannot find fd for {} in table", path);
            return -ErrorCodes.EBADFD();
        }
        if (oe.getOut() != null) {
            try {
                oe.getOut().flush();
            } catch (IOException e) {
                log.error("Failed to flush {}", path, e);
                return -ErrorCodes.EIO();
            }
        } else {
            log.debug("Not flushing: {} was not open for writing", path);
        }
        return 0;
    }

    /**
     * Releases the resources associated to an open file. Release() is async.
     * <p>
     * Guaranteed to be called once for each open() or create().
     *
     * @param path the FS path of the file to release
     * @param fi   FileInfo data structure kept by FUSE
     * @return 0. The return value is ignored by FUSE (any error should be reported
     * on flush instead)
     */
    @Override
    public int release(String path, FuseFileInfo fi) {
        log.trace("release({})", path);
        OpenFileEntry oe;
        final long fd = fi.fh.get();
        oe = mOpenFiles.getFirstByField(ID_INDEX, fd);
        mOpenFiles.remove(oe);
        if (oe == null) {
            log.error("Cannot find fd for {} in table", path);
            return -ErrorCodes.EBADFD();
        }
        try {
            oe.close();
        } catch (IOException e) {
            log.error("Failed closing {} [in]", path, e);
        }
        return 0;
    }

    /**
     * Resolves a FUSE path into {@link Path} and possibly keeps it in the cache.
     */
    private final class PathCacheLoader extends CacheLoader<String, Path> {

        /**
         * Constructs a new {@link PathCacheLoader}.
         */
        PathCacheLoader() {
        }

        @Override
        public Path load(String fusePath) {
            // fusePath is guaranteed to always be an absolute path (i.e., starts
            // with a fwd slash) - relative to the FUSE mount point
            String relPath = fusePath.substring(1);
            if (relPath.isEmpty()) {
                relPath = ".";
            }
            Path turi = new Path(rootPath, relPath);
            return turi;
        }
    }
}
