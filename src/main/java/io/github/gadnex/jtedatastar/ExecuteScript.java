package io.github.gadnex.jtedatastar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar ExecuteScript events
 *
 * <p>Executes JavaScript in the browser.
 */
public class ExecuteScript extends AbstractDatastarEmitter {

  private final Set<Map<String, String>> attributes;
  private final Set<String> scripts;
  private Boolean autoRemove;

  private static final String DATASTAR_EXECUTE_SCRIPT = " datastar-execute-script";
  private static final String AUTO_REMOVE = " autoRemove ";
  private static final String ATTRIBUTES = " attributes ";
  private static final String SCRIPT = " script ";

  /**
   * Constructor for creating the ExecuteScript emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public ExecuteScript(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
    attributes = new HashSet<>();
    scripts = new HashSet<>();
  }

  /**
   * Optionally determines whether to remove the script after execution. Default false.
   *
   * @param autoRemove The autoRemove value
   * @return The ExecuteScript object
   */
  public ExecuteScript autoRemove(boolean autoRemove) {
    this.autoRemove = autoRemove;
    return this;
  }

  /**
   * Optionally add a script attribute. Each attributes line adds an attribute (in the format name
   * value) to the script element.
   *
   * @param name The attribute name
   * @param value The attribute value
   * @return The ExecuteScript object
   */
  public ExecuteScript attribute(String name, String value) {
    attributes.add(Map.of(name, value));
    return this;
  }

  /**
   * Add at least 1 script line. Each script line contains JavaScript to be executed by the browser.
   *
   * @param script the script line
   * @return The ExecuteScript object
   */
  public ExecuteScript script(String script) {
    scripts.add(script);
    return this;
  }

  /**
   * Emit the SSE event
   *
   * @throws EmitException Exception if the event fails to be emitted
   */
  public void emit() throws EmitException {
    if (scripts.isEmpty()) {
      throw new IllegalStateException("No scripts specified");
    }
    event.name(DATASTAR_EXECUTE_SCRIPT);
    if (autoRemove != null) {
      event.data(AUTO_REMOVE + autoRemove);
    }
    for (Map<String, String> attribute : attributes) {
      String key = attribute.keySet().iterator().next();
      event.data(ATTRIBUTES + key + " " + attribute.get(key));
    }
    for (String script : scripts) {
      event.data(SCRIPT + script);
    }
    emitEvents();
  }
}
