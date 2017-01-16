package net.mbl.demo.authentication;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 * A client side callback to put application provided username/password into SASL transport.
 */
public final class PlainSaslClientCallbackHandler implements CallbackHandler {

  private final String mUserName;
  private final String mPassword;

  /**
   * Constructs a new client side callback.
   *
   * @param userName the name of the user
   * @param password the password
   */
  public PlainSaslClientCallbackHandler(String userName, String password) {
    mUserName = userName;
    mPassword = password;
  }

  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for (Callback callback : callbacks) {
      if (callback instanceof NameCallback) {
        NameCallback nameCallback = (NameCallback) callback;
        nameCallback.setName(mUserName);
      } else if (callback instanceof PasswordCallback) {
        PasswordCallback passCallback = (PasswordCallback) callback;
        passCallback.setPassword(mPassword == null ? null : mPassword.toCharArray());
      } else {
        Class<?> callbackClass = (callback == null) ? null : callback.getClass();
        throw new UnsupportedCallbackException(callback, callbackClass + " is unsupported.");
      }
    }
  }
}
