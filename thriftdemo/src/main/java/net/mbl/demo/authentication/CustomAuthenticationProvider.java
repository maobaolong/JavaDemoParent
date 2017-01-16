package net.mbl.demo.authentication;


import net.mbl.demo.util.CommonUtils;

import javax.annotation.concurrent.NotThreadSafe;
import javax.security.sasl.AuthenticationException;


@NotThreadSafe
public final class CustomAuthenticationProvider implements AuthenticationProvider {

  private final AuthenticationProvider mCustomProvider;

  /**
   * Constructs a new custom authentication provider.
   *
   * @param providerName the name of the provider
   */
  public CustomAuthenticationProvider(String providerName) {
    Class<?> customProviderClass;
    try {
      customProviderClass = Class.forName(providerName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(providerName + " not found");
    }

    try {
      mCustomProvider = (AuthenticationProvider) CommonUtils
          .createNewClassInstance(customProviderClass, null, null);
    } catch (Exception e) {
      throw new RuntimeException(
          customProviderClass.getName() + " instantiate failed :" + e.getMessage());
    }
  }

  /**
   * @return the custom authentication provider
   */
  public AuthenticationProvider getCustomProvider() {
    return mCustomProvider;
  }

  @Override
  public void authenticate(String user, String password) throws AuthenticationException {
    mCustomProvider.authenticate(user, password);
  }
}
