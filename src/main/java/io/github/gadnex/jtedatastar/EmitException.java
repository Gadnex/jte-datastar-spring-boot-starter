package io.github.gadnex.jtedatastar;

import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Custom exception thrown when Datastar events cannot be emitted for technical reasons. These
 * checked exceptions need to be handled by the calling application.
 *
 * <p>In common cases the client HTTP connection of the SSE emitter is no longer open and thus the
 * client application should not attempt to emit future SSE events to the failed SSE emitters.
 */
public class EmitException extends Exception {

  /** The set of SSE emitters that caused the emit exception */
  private final Set<SseEmitter> sseEmitters;

  /**
   * Constructor for the EmitException
   *
   * @param message The text message for the exception
   * @param sseEmitters The SSE emitter that failed to emit the SSE event
   */
  public EmitException(String message, Set<SseEmitter> sseEmitters) {
    super(message);
    this.sseEmitters = sseEmitters;
  }

  /**
   * Get the SSE emitter that caused the exception. Required to identify which SSe emitter in the
   * collection caused the exception.
   *
   * @return The SSE emitters
   */
  public Set<SseEmitter> emitters() {
    return sseEmitters;
  }
}
