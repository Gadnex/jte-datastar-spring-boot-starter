package io.github.gadnex.jtedatastar;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** JTE configuration properties */
@ConfigurationProperties(prefix = "gg.jte.localizer")
public class JteProperties {

  private boolean inject = false;

  /** Default constructor */
  public JteProperties() {}

  /**
   * Get inject property
   *
   * @return Inject property
   */
  public boolean isInject() {
    return inject;
  }

  /**
   * Set inject property
   *
   * @param inject Inject propert
   */
  public void setInject(boolean inject) {
    this.inject = inject;
  }
}
