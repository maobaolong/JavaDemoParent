package net.mbl.demo.web;



import net.mbl.demo.Constants;
import net.mbl.demo.util.PathUtils;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.net.InetSocketAddress;

import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletException;

/**
 * The proxy web server.
 */
@NotThreadSafe
public final class ProxyWebServer extends WebServer {
  /**
   * Creates a new instance of {@link ProxyWebServer}.
   *
   * @param serviceName the service name
   * @param address the service address
   */
  public ProxyWebServer(String serviceName, InetSocketAddress address) {
    super(serviceName, address);

    // REST configuration
    ResourceConfig config = new ResourceConfig().packages("net.mbl.demo.proxy");
    ServletContainer servlet = new ServletContainer(config) {
      private static final long serialVersionUID = 7756010860672831556L;

      @Override
      public void init() throws ServletException {
        super.init();
      }
    };
    ServletHolder servletHolder = new ServletHolder("Proxy Web Service", servlet);
    mWebAppContext.addServlet(servletHolder, PathUtils.concatPath(Constants.REST_API_PREFIX, "*"));
  }
}
