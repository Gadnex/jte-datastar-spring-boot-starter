package io.github.gadnex.jtedatastar;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar RemoveSignals events
 *
 * <p>Removes signals that match one or more provided paths.
 */
public class RemoveSignals extends AbstractDatastarEmitter {

  private final Set<String> paths;
  private static final String DATASTAR_REMOVE_SIGNALS = " datastar-remove-signals";
  private static final String PATHS = " paths ";

  /**
   * Constructor for creating the RemoveSignals emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public RemoveSignals(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
    this.paths = new HashSet<>();
  }

  /**
   * Add a signal path to be removed
   *
   * @param path The signal path
   * @return The RemoveSignals object
   */
  public RemoveSignals path(String path) {
    paths.add(path.trim());
    return this;
  }

  /**
   * Emit the SSE event
   *
   * @throws EmitException Exception if the event fails to be emitted
   */
  public void emit() throws EmitException {
    if (paths.isEmpty()) {
      throw new IllegalStateException("No paths specified");
    }
    event.name(DATASTAR_REMOVE_SIGNALS);
    for (String path : paths) {
      event.data(PATHS + path);
    }
    emitEvents();
  }
}
