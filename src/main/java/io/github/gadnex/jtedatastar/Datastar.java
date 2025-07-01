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
   * @param templateEngine The JTE template engine used to render HTML PatchElements
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
   * Construct a PatchElements object with a collection of SSE emitters.
   *
   * @param sseEmitters Set of SSE emitters
   * @return The PatchElements object
   */
  public PatchElements patchElements(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new PatchElements(templateEngine, templateSuffix, sseEmitters, messageSource);
  }

  /**
   * Construct a PatchElements object with a single SSE emitter.
   *
   * @param sseEmitter The SSE emitter
   * @return The PatchElements object
   */
  public PatchElements patchElements(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return patchElements(Set.of(sseEmitter));
  }

  /**
   * Construct a PatchSignals object with a collection of SSE emitters
   *
   * @param sseEmitters Set of SSE emitters
   * @return The PatchSignals object
   */
  public PatchSignals patchSignals(Set<SseEmitter> sseEmitters) {
    if (sseEmitters == null || sseEmitters.isEmpty()) {
      throw new IllegalArgumentException("sseEmitters is null or empty");
    }
    return new PatchSignals(sseEmitters);
  }

  /**
   * Construct a PatchSignals object with a single SSE emitter
   *
   * @param sseEmitter The SSE emitter
   * @return The PatchSignals object
   */
  public PatchSignals patchSignals(SseEmitter sseEmitter) {
    if (sseEmitter == null) {
      throw new IllegalArgumentException("sseEmitter is null");
    }
    return patchSignals(Set.of(sseEmitter));
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
