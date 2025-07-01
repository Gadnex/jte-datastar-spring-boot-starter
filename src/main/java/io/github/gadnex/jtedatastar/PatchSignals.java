package io.github.gadnex.jtedatastar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar PatchSignals events.
 *
 * <p>Patches the signal values in the browser.
 */
public class PatchSignals extends AbstractDatastarEmitter {

  private final Map<String, Object> signals;
  private final ObjectMapper objectMapper;
  private Boolean onlyIfMissing;

  private static final String DATASTAR_PATCH_SIGNALS = " datastar-patch-signals";
  private static final String ONLY_IF_MISSING = " onlyIfMissing ";
  private static final String SIGNALS = " signals ";

  /**
   * Constructor for creating the PatchSignals emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public PatchSignals(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
    signals = new HashMap<>();
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Determines whether to update the signals with new values only if the key does not exist.
   *
   * @param onlyIfMissing The onlyIfMissing value
   * @return The PatchSignals object
   */
  public PatchSignals onlyIfMissing(boolean onlyIfMissing) {
    this.onlyIfMissing = onlyIfMissing;
    return this;
  }

  /**
   * Specify one or more signals to emit. Should be a valid data-signals attribute.
   *
   * @param name The signal name
   * @param value The signal value
   * @return The PatchSignals object
   */
  public PatchSignals signal(String name, Object value) {
    signals.put(name, value);
    return this;
  }

  /** Emit the SSE event */
  public void emit() {
    if (signals.isEmpty()) {
      throw new IllegalStateException("No signals specified");
    }
    event.name(DATASTAR_PATCH_SIGNALS);
    if (onlyIfMissing != null) {
      event.data(ONLY_IF_MISSING + onlyIfMissing);
    }
    try {
      String signalsString = objectMapper.writeValueAsString(signals);
      event.data(SIGNALS + signalsString);
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("cannot convert signals to JSON", ex);
    }
    emitEvents();
  }
}
