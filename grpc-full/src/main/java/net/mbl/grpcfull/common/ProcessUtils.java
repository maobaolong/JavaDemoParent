package net.mbl.grpcfull.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for {@link Process}es.
 */
public final class ProcessUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessUtils.class);

    /**
     * Runs the given {@link Process}. This method should only be called from {@code main()} methods.
     *
     * @param process the {@link Process} to run
     */
    public static void run(Process process) {
        try {
            LOG.info("Starting {}.", process);
            process.start();
            LOG.info("Stopping {}.", process);
            System.exit(0);
        } catch (Throwable t) {
            LOG.error("Uncaught exception while running {}, stopping it and exiting. "
                    + "Exception \"{}\", Root Cause \"{}\"", process, t, ExceptionUtils.getRootCause(t), t);
            try {
                process.stop();
            } catch (Throwable t2) {
                // continue to exit
                LOG.error("Uncaught exception while stopping {}, simply exiting. "
                                + "Exception \"{}\", Root Cause \"{}\"", process, t2, ExceptionUtils.getRootCause(t2),
                        t2);
            }
            System.exit(-1);
        }
    }

    /**
     * Logs a fatal error and then exits the system.
     *
     * @param logger the logger to log to
     * @param format the error message format string
     * @param args   args for the format string
     */
    public static void fatalError(Logger logger, String format, Object... args) {
        fatalError(logger, null, format, args);
    }

    /**
     * Logs a fatal error and then exits the system.
     *
     * @param logger the logger to log to
     * @param t      the throwable causing the fatal error
     * @param format the error message format string
     * @param args   args for the format string
     */
    public static void fatalError(Logger logger, Throwable t, String format, Object... args) {
        String message = String.format("Fatal error: " + format, args);
        if (t != null) {
            message += "\n" + ExceptionUtils.getStackTrace(t);
        }

        logger.error(message);
        System.exit(-1);
    }

    /**
     * Adds a shutdown hook that will be invoked when a signal is sent to this process.
     * <p>
     * The process may be utilizing some resources, and this shutdown hook will be invoked by
     * JVM when a SIGTERM is sent to the process by "kill" command. The shutdown hook calls
     * {@link Process#stop()} method to cleanly release the resources and exit.
     *
     * @param process the data structure representing the process to terminate
     */
    public static void stopProcessOnShutdown(final Process process) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                process.stop();
            } catch (Throwable t) {
                LOG.error("Failed to stop process", t);
            }
        }, "process-shutdown-hook"));
    }

    private ProcessUtils() {
    } // prevent instantiation
}
