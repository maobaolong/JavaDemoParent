package net.mbl.demo.web;


import net.mbl.demo.Constants;
import net.mbl.demo.util.PathUtils;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletException;
import java.net.InetSocketAddress;

/**
 * The master web server.
 */
@NotThreadSafe
public final class MasterWebServer extends WebServer {

  /**
   * Creates a new instance of {@link MasterWebServer}.
   *
   * @param serviceName the service name
   * @param address the service address
   */
  public MasterWebServer(String serviceName, InetSocketAddress address
                         ) {
    super(serviceName, address);

    mWebAppContext.addServlet(new ServletHolder(new WebInterfaceGeneralServlet()), "/home");
//    mWebAppContext.addServlet(new ServletHolder(new WebInterfaceBrowseAjaxServlet()), "/browse/jumpPath.ajax");

    // REST configuration
    ResourceConfig config = new ResourceConfig().packages("");
    // Override the init method to inject a reference to MyWebServer into the servlet context.
    // ServletContext may not be modified until after super.init() is called.
    ServletContainer servlet = new ServletContainer(config) {
      private static final long serialVersionUID = 7756010860672831556L;

      @Override
      public void init() throws ServletException {
        super.init();
      }
    };

    ServletHolder servletHolder = new ServletHolder("Master Web Service", servlet);
    mWebAppContext.addServlet(servletHolder, PathUtils.concatPath(Constants.REST_API_PREFIX, "*"));
  }
}
