package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
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
   * Construct a MergeFragments object with a collection of SSE emitters.
   *
   * @param sseEmitters Set of SSE emitters
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new MergeFragments(templateEngine, templateSuffix, sseEmitters, messageSource);
  }

  /**
   * Construct a MergeFragments object with a single SSE emitter.
   *
   * @param sseEmitter The SSE emitter
   * @return The MergeFragments object
   */
  public MergeFragments mergeFragments(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return mergeFragments(Set.of(sseEmitter));
  }

  /**
   * Construct a RemoveFragments object with a collection of SSE emitters
   *
   * @param sseEmitters Set of SSE emitters
   * @return The RemoveFragments object
   */
  public RemoveFragments removeFragments(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new RemoveFragments(sseEmitters);
  }

  /**
   * Construct a RemoveFragments object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The RemoveFragments object
   */
  public RemoveFragments removeFragments(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return removeFragments(Set.of(sseEmitter));
  }

  /**
   * Construct a MergeSignals object with a collection of SSE emitters
   *
   * @param sseEmitters Set of SSE emitters
   * @return The MergeSignals object
   */
  public MergeSignals mergeSignals(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new MergeSignals(sseEmitters);
  }

  /**
   * Construct a MergeSignals object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The MergeSignals object
   */
  public MergeSignals mergeSignals(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return mergeSignals(Set.of(sseEmitter));
  }

  /**
   * Construct a RemoveSignals object with a collection of SSE emitters
   *
   * @param sseEmitters Set of SSE emitters
   * @return The RemoveSignals object
   */
  public RemoveSignals removeSignals(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new RemoveSignals(sseEmitters);
  }

  /**
   * Construct a RemoveSignals object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The RemoveSignals object
   */
  public RemoveSignals removeSignals(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return removeSignals(Set.of(sseEmitter));
  }

  /**
   * Construct an ExecuteScript object with a collection of SSE emitters
   *
   * @param sseEmitters Set of SSE emitters
   * @return The ExecuteScript object
   */
  public ExecuteScript executeScript(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new ExecuteScript(sseEmitters);
  }

  /**
   * Construct an ExecuteScript object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The ExecuteScript object
   */
  public ExecuteScript executeScript(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return executeScript(Set.of(sseEmitter));
  }
}
