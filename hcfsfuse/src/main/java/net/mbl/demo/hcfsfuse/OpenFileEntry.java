package net.mbl.demo.hcfsfuse;

import com.google.common.base.Preconditions;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.Closeable;
import java.io.IOException;

/**
 * Convenience class to encapsulate input/output streams of open target files.
 * <p>
 * An open file can be either write-only or read-only, never both. This means that one of getIn or
 * getOut will be null, while the other will be non-null. It is up to the user of this class
 * (currently, only {@link FuseFileSystem}) to check that.
 * <p>
 * This mechanism is preferred over more complex sub-classing to avoid useless casts or type checks
 * for every read/write call, which happen quite often.
 */
@NotThreadSafe
final class OpenFileEntry implements Closeable {
    private final long mId;
    private final FSDataInputStream mIn;
    private final FSDataOutputStream mOut;

    // Path is likely to be changed when fuse rename() is called
    private String mPath;
    /**
     * the next write offset.
     */
    private long mOffset;

    /**
     * Constructs a new {@link OpenFileEntry} for an target file.
     *
     * @param id   the id of the file
     * @param path the path of the file
     * @param in   the input stream of the file
     * @param out  the output stream of the file
     */
    OpenFileEntry(long id, String path, FSDataInputStream in, FSDataOutputStream out) {
        Preconditions.checkArgument(id != -1 && !path.isEmpty());
        Preconditions.checkArgument(in != null || out != null);
        mId = id;
        mIn = in;
        mOut = out;
        mPath = path;
        mOffset = -1;
    }

    /**
     * @return the id of the file
     */
    public long getId() {
        return mId;
    }

    /**
     * @return the path of the file
     */
    public String getPath() {
        return mPath;
    }

    /**
     * Gets the opened input stream for this open file entry. The value returned can be {@code null}
     * if the file is not open for reading.
     *
     * @return an opened input stream for the open target file, or null
     */
    @Nullable
    public FSDataInputStream getIn() {
        return mIn;
    }

    /**
     * Gets the opened output stream for this open file entry. The value returned can be {@code null}
     * if the file is not open for writing.
     *
     * @return an opened input stream for the open target file, or null
     */
    @Nullable
    public FSDataOutputStream getOut() {
        return mOut;
    }

    /**
     * @return the offset of the next write
     */
    public long getWriteOffset() {
        return mOffset;
    }

    /**
     * Sets the path of the file. The file path can be changed
     * if fuse rename() is called.
     *
     * @param path the new path of the file
     */
    public void setPath(String path) {
        mPath = path;
    }

    /**
     * Sets the offset of the next write.
     */
    public void setWriteOffset(long offset) {
        mOffset = offset;
    }

    /**
     * Closes the underlying open streams.
     */
    @Override
    public void close() throws IOException {
        if (mIn != null) {
            mIn.close();
        }

        if (mOut != null) {
            mOut.close();
        }
        mOffset = -1;
    }
}
