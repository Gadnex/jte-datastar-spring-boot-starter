package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A Spring Bean class used to construct instances of Datastar Server Sent Event (SSE) types
 * supported by the Datastar front end Javascript library.
 */
public class Datastar {

  private final TemplateEngine templateEngine;
  private final String templateSuffix;
  private final MessageSource messageSource;

  /**
   * Constructor for the Datastar Spring Bean
   *
   * @param templateEngine The JTE template engine used to render HTML MergeFragments
   * @param templateSuffix The template suffix used for JTE template files
   * @param messageSource The Spring MessageSource used for getting language specific text for
   *     template rendering
   */
  public Datastar(
      TemplateEngine templateEngine, String templateSuffix, MessageSource messageSource) {
    this.templateEngine = templateEngine;
    this.templateSuffix = templateSuffix;
    this.messageSource = messageSource;
  }

  /**
   * Construct a MergeFragments object with a collection of SSE emitters and a Locale for rendering
   * JTE templates in a specific language.
   *
   * @param sseEmitters Collection of SSE emitters
   * @param locale The locale for JTE template rendering
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(Collection<SseEmitter> sseEmitters, Locale locale) {
    return new MergeFragments(templateEngine, templateSuffix, sseEmitters, messageSource, locale);
  }

  /**
   * Construct a MergeFragments object with a collection of SSE emitters and using the default
   * Locale for rendering the JTE template.
   *
   * @param sseEmitters Collection of SSE emitters
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(Collection<SseEmitter> sseEmitters) {
    return mergeFragments(sseEmitters, Locale.getDefault());
  }

  /**
   * Construct a MergeFragments object with a single SSE emitter and a Locale for rendering JTE
   * template in a specific language.
   *
   * @param sseEmitter The SSE emitter
   * @param locale The locale for JTE template rendering
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(SseEmitter sseEmitter, Locale locale) {
    return mergeFragments(Set.of(sseEmitter), locale);
  }

  /**
   * Construct a MergeFragments object with a single SSE emitter and using the default Locale for
   * rendering the JTE template.
   *
   * @param sseEmitter The SSE emitter
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(SseEmitter sseEmitter) {
    return mergeFragments(Set.of(sseEmitter), Locale.getDefault());
  }

  /**
   * Construct a RemoveFragments object with a collection of SSE emitters
   *
   * @param sseEmitters Collection of SSE emitters
   * @return The RemoveFragments object
   */
  public RemoveFragments removeFragments(Set<SseEmitter> sseEmitters) {
    return new RemoveFragments(sseEmitters);
  }

  /**
   * Construct a RemoveFragments object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The RemoveFragments object
   */
  public RemoveFragments removeFragments(SseEmitter sseEmitter) {
    return removeFragments(Set.of(sseEmitter));
  }

  /**
   * Construct a MergeSignals object with a collection of SSE emitters
   *
   * @param sseEmitters Collection of SSE emitters
   * @return The MergeSignals object
   */
  public MergeSignals mergeSignals(Set<SseEmitter> sseEmitters) {
    return new MergeSignals(sseEmitters);
  }

  /**
   * Construct a MergeSignals object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The MergeSignals object
   */
  public MergeSignals mergeSignals(SseEmitter sseEmitter) {
    return mergeSignals(Set.of(sseEmitter));
  }

  /**
   * Construct a RemoveSignals object with a collection of SSE emitters
   *
   * @param sseEmitters Collection of SSE emitters
   * @return The RemoveSignals object
   */
  public RemoveSignals removeSignals(Set<SseEmitter> sseEmitters) {
    return new RemoveSignals(sseEmitters);
  }

  /**
   * Construct a RemoveSignals object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The RemoveSignals object
   */
  public RemoveSignals removeSignals(SseEmitter sseEmitter) {
    return removeSignals(Set.of(sseEmitter));
  }

  /**
   * Construct an ExecuteScript object with a collection of SSE emitters
   *
   * @param sseEmitters Collection of SSE emitters
   * @return The ExecuteScript object
   */
  public ExecuteScript executeScript(Set<SseEmitter> sseEmitters) {
    return new ExecuteScript(sseEmitters);
  }

  /**
   * Construct an ExecuteScript object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The ExecuteScript object
   */
  public ExecuteScript executeScript(SseEmitter sseEmitter) {
    return executeScript(Set.of(sseEmitter));
  }
}
