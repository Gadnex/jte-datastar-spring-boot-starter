package io.github.gadnex.jtedatastar;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** JTE Datastar Server Sent Event (SSE) configuration properties */
@ConfigurationProperties(prefix = "datastar.sse")
public class JteDatastarSseProperties {

  private boolean enabled = true;

  /** Default constructor */
  public JteDatastarSseProperties() {}

  /**
   * Get SSE enabled property
   *
   * @return SSE enabled property
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Set SSE enabled property
   *
   * @param enabled true to enable and false to disable
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
