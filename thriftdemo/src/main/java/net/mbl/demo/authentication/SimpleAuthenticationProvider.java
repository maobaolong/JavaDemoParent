package net.mbl.demo.authentication;

import javax.annotation.concurrent.ThreadSafe;
import javax.security.sasl.AuthenticationException;

/**
 * An authentication provider implementation that allows arbitrary combination of username and
 * password including empty strings.
 */
@ThreadSafe
public final class SimpleAuthenticationProvider implements AuthenticationProvider {

  /**
   * Constructs a new {@link SimpleAuthenticationProvider}.
   */
  public SimpleAuthenticationProvider() {}

  @Override
  public void authenticate(String user, String password) throws AuthenticationException {
    // no-op authentication
  }
}
