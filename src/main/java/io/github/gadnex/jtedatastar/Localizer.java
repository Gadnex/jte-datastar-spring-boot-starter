package io.github.gadnex.jtedatastar;

import gg.jte.support.LocalizationSupport;
import java.util.Locale;
import org.springframework.context.MessageSource;

/**
 * An implementation of the JTE LocalizationSupport interface to get language specific text using
 * the Spring MessageSource.
 */
public class Localizer implements LocalizationSupport {

  private final MessageSource messageSource;
  private final Locale locale;

  /**
   * Constructor for the Localizer
   *
   * @param messageSource The Spring MessageSource
   * @param locale The locale
   */
  public Localizer(final MessageSource messageSource, final Locale locale) {
    this.messageSource = messageSource;
    this.locale = locale;
  }

  @Override
  public String lookup(String key) {
    return messageSource.getMessage(key, null, locale);
  }
}
