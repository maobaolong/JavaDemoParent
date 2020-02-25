package net.mbl.demo.hcfsfuse;

import org.junit.Test;

import java.io.IOException;

public class HcfsFuseTest {
    @Test
    public void testFuseLocal() throws IOException {
        HCFSFuse.main(new String[] {"-m", "/Users/mbl/fusefs", "-r", "file:///tmp/", "-debug"});
    }
}
