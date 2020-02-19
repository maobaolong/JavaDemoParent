package net.mbl.grpcfull.example;

import lombok.extern.slf4j.Slf4j;
import net.mbl.grpcfull.common.ProcessUtils;

/**
 * Entry point for the master.
 */
@Slf4j
public final class Master {

    private Master() {
    } // prevent instantiation

    /**
     * Starts the Process.
     *
     * @param args command line arguments, should be empty
     */
    public static void main(String[] args) {
        MasterProcess process;
        try {
            process = new MasterProcess();
        } catch (Throwable t) {
            ProcessUtils.fatalError(log, t, "Failed to create process");
            // fatalError will exit, so we shouldn't reach here.
            throw t;
        }

        ProcessUtils.stopProcessOnShutdown(process);
        ProcessUtils.run(process);
    }
}
