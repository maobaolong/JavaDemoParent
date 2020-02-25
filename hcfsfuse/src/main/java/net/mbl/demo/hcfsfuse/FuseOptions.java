package net.mbl.demo.hcfsfuse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class FuseOptions {
    @Getter
    private final String mountPoint;
    @Getter
    private final String root;
    @Getter
    private final boolean debug;
    @Getter
    private final List<String> fuseOpts;
    @Getter
    private final String confPath;
}
