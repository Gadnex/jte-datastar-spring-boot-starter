package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/** Spring Boot AutoConfiguration class */
@AutoConfiguration
public class JteDatastarAutoConfiguration {

  private final TemplateEngine templateEngine;
  private final String templateSuffix;
  private final MessageSource messageSource;

  /**
   * Constructor for auto configuration
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
  public Datastar dataStar() {
    return new Datastar(templateEngine, templateSuffix, messageSource);
  }
}
