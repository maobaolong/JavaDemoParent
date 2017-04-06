package net.mbl.demo.proxy;

import com.google.common.base.Function;
import net.mbl.demo.Constants;
import net.mbl.demo.Server;
import net.mbl.demo.util.CommonUtils;
import net.mbl.demo.web.ProxyWebServer;
import net.mbl.demo.web.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.NotThreadSafe;
import java.net.InetSocketAddress;

/**
 * Entry point for the proxy program.
 */
@NotThreadSafe
public final class MyProxy implements Server {
    private static final Logger LOG = LoggerFactory.getLogger(Constants.LOGGER_TYPE);

    /**
     * Starts the proxy.
     *
     * @param args command line arguments, should be empty
     */
    public static void main(String[] args) {
        MyProxy proxy = new MyProxy();
        try {
            proxy.start();
        } catch (Exception e) {
            LOG.error("Uncaught exception while running proxy, stopping it and exiting.", e);
            try {
                proxy.stop();
            } catch (Exception e2) {
                // continue to exit
                LOG.error("Uncaught exception while stopping proxy, simply exiting.", e2);
            }
            System.exit(-1);
        }
    }

    /** The web server. */
    private WebServer mWebServer = null;

    /**
     * Creates an instance of {@link MyProxy}.
     */
    public MyProxy() {}

    /**
     * @return the actual port that the web service is listening on (used by unit test only)
     */
    public int getWebLocalPort() {
        if (mWebServer != null) {
            return mWebServer.getLocalPort();
        }
        return -1;
    }

    @Override
    public void start() throws Exception {
        mWebServer = new ProxyWebServer("ProxyWebServer",new InetSocketAddress("localhost",
            1235));
        // start web server
        mWebServer.start();
    }

    @Override
    public void stop() throws Exception {
        mWebServer.stop();
        mWebServer = null;
    }

    /**
     * Blocks until the proxy is ready to serve requests.
     */
    public void waitForReady() {
        CommonUtils.waitFor("proxy web server", new Function<Void, Boolean>() {
            @Override
            public Boolean apply(Void input) {
                return mWebServer != null && mWebServer.getServer().isRunning();
            }
        });
    }
}
