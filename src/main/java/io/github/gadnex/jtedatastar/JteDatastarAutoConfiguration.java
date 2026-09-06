package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
import io.github.gadnex.jtedatastar.csp.CspHeaderFallbackFilter;
import io.github.gadnex.jtedatastar.csp.CspNonceModelAdvice;
import io.github.gadnex.jtedatastar.csp.CspRequestNonceFilter;
import io.github.gadnex.jtedatastar.sse.Datastar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Spring Boot AutoConfiguration class */
@AutoConfiguration(
    afterName = {
      "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
      "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"
    })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties({JteDatastarCspProperties.class, JteDatastarSseProperties.class})
public class JteDatastarAutoConfiguration {

  private final TemplateEngine templateEngine;
  private final String templateSuffix;
  private final MessageSource messageSource;

  /**
   * Constructor for autoconfiguration
   *
   * @param templateEngine The JTE template engine used to render HTML fragments
   * @param templateSuffix The templte suffix for JTE template files
   * @param messageSource Spring Boot MessageSource for localization
   */
  public JteDatastarAutoConfiguration(
      TemplateEngine templateEngine,
      @Value("${gg.jte.templateSuffix:.jte}") String templateSuffix,
      MessageSource messageSource) {
    this.templateEngine = templateEngine;
    this.templateSuffix = templateSuffix;
    this.messageSource = messageSource;
  }

  /**
   * A Datastar Spring Bean that is used to easily construct Datastar SSE event emitters
   *
   * @return The Datastar bean
   */
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(
      prefix = "datastar.sse",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  public Datastar dataStar() {
    return new Datastar(templateEngine, templateSuffix, messageSource);
  }

  /**
   * CspRequestNonceFilter Spring Bean filter that generates and binds the CSP nonce to the request
   * lifecycle
   *
   * @return The CspRequestNonceFilter Spring Bean
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  @ConditionalOnProperty(
      prefix = "datastar.csp",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = false)
  public CspRequestNonceFilter cspRequestNonceFilter() {
    return new CspRequestNonceFilter();
  }

  /**
   * CspHeaderFallbackFilter Spring Bean filter that ads a CSP header to the HTTP response
   *
   * @return The CspHeaderFallbackFilter Spring Bean
   */
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE + 1)
  @ConditionalOnMissingBean(type = "org.springframework.security.web.SecurityFilterChain")
  @ConditionalOnProperty(
      prefix = "datastar.csp",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = false)
  @ConditionalOnProperty(
      prefix = "datastar.csp",
      name = "csp-header",
      havingValue = "true",
      matchIfMissing = false)
  public CspHeaderFallbackFilter cspHeaderFallbackFilter() {
    return new CspHeaderFallbackFilter();
  }

  /**
   * CspNonceModelAdvice Spring Bean that adds a nonce parameter to the MVC model
   *
   * @return The CspNonceModelAdvice Spring Bean
   */
  @Bean
  @ConditionalOnProperty(
      prefix = "datastar.csp",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = false)
  public CspNonceModelAdvice cspNonceModelAdvice() {
    return new CspNonceModelAdvice();
  }
}
