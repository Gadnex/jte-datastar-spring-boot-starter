package io.github.gadnex.jtedatastar;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** JTE Datastar Content-Security-Policy (CSP) configuration properties */
@ConfigurationProperties(prefix = "datastar.csp")
public class JteDatastarCspProperties {

  private boolean enabled = false;
  private boolean cspHeader = false;

  /** Default constructor */
  public JteDatastarCspProperties() {}

  /**
   * Get CSP enabled property
   *
   * @return CSP enabled property
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Set CSP enabled property
   *
   * @param enabled true to enable and false to disable
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Get CSP Header property
   *
   * @return CSP Header property
   */
  public boolean isCspHeader() {
    return cspHeader;
  }

  /**
   * Set CSP Header property
   *
   * @param cspHeader true to enable and false to disable
   */
  public void setCspHeader(boolean cspHeader) {
    this.cspHeader = cspHeader;
  }
}
