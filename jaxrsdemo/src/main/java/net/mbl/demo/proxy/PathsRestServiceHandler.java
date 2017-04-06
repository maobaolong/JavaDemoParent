package net.mbl.demo.proxy;

import com.qmino.miredot.annotations.ReturnType;
import net.mbl.demo.RestUtils;

import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class is a REST handler for path resources.
 */
@NotThreadSafe
@Path(PathsRestServiceHandler.SERVICE_PREFIX)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class PathsRestServiceHandler {
  public static final String SERVICE_PREFIX = "paths";

  public static final String PATH_PARAM = "{path:.*}/";

  public static final String CREATE_DIRECTORY = "create-directory";
  public static final String EXISTS = "exists";


  /**
   * Constructs a new {@link PathsRestServiceHandler}.
   *
   * @param context context for the servlet
   */
  public PathsRestServiceHandler(@Context ServletContext context) {

  }

  /**
   * @summary creates a directory
   * @param path the path
   * @return the response object
   */
  @POST
  @Path(PATH_PARAM + CREATE_DIRECTORY)
  @ReturnType("java.lang.Void")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createDirectory(@PathParam("path") final String path) {
    return RestUtils.call(new RestUtils.RestCallable<String>() {
      @Override
      public String call() throws Exception {

        return path;
      }
    });
  }



  /**
   * @summary checks whether a path exists
   * @param path the path
   * @return the response object
   */
  @POST
  @Path(PATH_PARAM + EXISTS)
  @ReturnType("java.lang.Boolean")
  public Response exists(@PathParam("path") final String path) {
    return RestUtils.call(new RestUtils.RestCallable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        return true;
      }
    });
  }

}
