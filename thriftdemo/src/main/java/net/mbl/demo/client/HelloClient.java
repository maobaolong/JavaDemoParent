package net.mbl.demo.client;

import net.mbl.demo.Constants;
import net.mbl.demo.exception.MyException;
import net.mbl.demo.thrift.MyTException;
import net.mbl.demo.thrift.HelloWorldService;
import org.apache.thrift.TException;

import javax.annotation.concurrent.ThreadSafe;
import javax.security.auth.Subject;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * A wrapper for the thrift client to interact with the file system master, used by clients.
 *
 * Since thrift clients are not thread safe, this class is a wrapper to provide thread safety, and
 * to provide retries.
 */
@ThreadSafe
public class HelloClient extends AbstractClient {
  private HelloWorldService.Client mClient = null;

  /**
   * Creates a new file system master client.
   *
   * @param subject the subject
   * @param masterAddress the master address
   */
  public HelloClient(Subject subject, InetSocketAddress masterAddress) {
    super(subject, masterAddress,"master");
  }

  /**
   * Creates a new file system master client.
   *
   * @param masterAddress the master address
   */
  public HelloClient(InetSocketAddress masterAddress) {

    super(null, masterAddress,"master");
  }

  @Override
  protected HelloWorldService.Client getClient() {
    return mClient;
  }

  @Override
  protected String getServiceName() {
    return Constants.HELLO_CLIENT_SERVICE_NAME;
  }

  @Override
  protected long getServiceVersion() {
    return Constants.HELLO_CLIENT_SERVICE_VERSION;
  }

  @Override
  protected void afterConnect() throws IOException {
    mClient = new HelloWorldService.Client(mProtocol);
  }
  public String sayHello(final String username) throws IOException, MyException {
    return retryRPC(new RpcCallableThrowsMyTException<String>() {
      @Override
      public String call() throws MyTException, TException {
        return mClient.sayHello(username);
      }
    });
  }

}
