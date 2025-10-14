package io.github.gadnex.jtedatastar;

import java.util.Set;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/** Abstract parent class for all Datastar emitters to implement common features */
public abstract class AbstractDatastarEmitter {

  /** Set of all SSE emitters that the event will be emitted to */
  protected Set<SseEmitter> sseEmitters;

  /** The event that will be emitted */
  protected SseEmitter.SseEventBuilder event;

  /**
   * Constructor
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  protected AbstractDatastarEmitter(Set<SseEmitter> sseEmitters) {
    this.sseEmitters = Set.copyOf(sseEmitters);
    this.event = SseEmitter.event();
    this.event.id(UUID.randomUUID().toString());
  }

  /** Emit the event to all SSE emitters */
  protected void emitEvents() {
    for (SseEmitter sseEmitter : sseEmitters) {
      try {
        sseEmitter.send(event);
      } catch (Exception ex) {
        sseEmitter.completeWithError(ex);
      }
    }
  }
}
