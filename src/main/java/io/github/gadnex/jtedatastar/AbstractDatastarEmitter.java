package io.github.gadnex.jtedatastar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/** Abstract parent class for all Datastar emitters to implement common features */
public abstract class AbstractDatastarEmitter {

  /** Collection of all SSE emitters that the event will be emitted to */
  protected Collection<SseEmitter> sseEmitters;

  /** The event that will be emitted */
  protected SseEmitter.SseEventBuilder event;

  /**
   * Constructor
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  protected AbstractDatastarEmitter(Collection<SseEmitter> sseEmitters) {
    this.sseEmitters = sseEmitters;
    this.event = SseEmitter.event();
    event.id(UUID.randomUUID().toString());
  }

  /**
   * Emit the event to all SSE emitters
   *
   * @throws EmitException Exception if the event fails to be emitted
   */
  protected void emitEvents() throws EmitException {
    Set<SseEmitter> failedSseEmitters = new HashSet<SseEmitter>();
    for (SseEmitter sseEmitter : sseEmitters) {
      try {
        sseEmitter.send(event);
      } catch (Exception ex) {
        failedSseEmitters.add(sseEmitter);
      }
    }
    if (!failedSseEmitters.isEmpty()) {
      throw new EmitException("Error sending to emitter", failedSseEmitters);
    }
  }
}
