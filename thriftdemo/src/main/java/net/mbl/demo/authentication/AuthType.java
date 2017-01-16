package net.mbl.demo.authentication;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Different authentication types .
 */
@ThreadSafe
public enum AuthType {
  /**
   * Authentication is disabled. No user info.
   */
  NOSASL,

  /**
   * User is aware . On the client side, the login username is determined by the
   * "security.login.username" property, or the OS user upon failure.
   * On the server side, the verification of client user is disabled.
   */
  SIMPLE,

  /**
   * User is aware . On the client side, the login username is determined by the
   * "security.login.username" property, or the OS user upon failure.
   * On the server side, the user is verified by a Custom authentication provider
   */
  CUSTOM,

  /**
   * User is aware . The user is verified by Kerberos authentication. NOTE: this
   * authentication is not supported.
   */
  KERBEROS,
  ;

  /**
   * @return the string value of AuthType
   */
  public String getAuthName() {
    return name();
  }
}
