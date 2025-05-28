package io.github.gadnex.jtedatastar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar ExecuteScript events
 *
 * <p>Executes JavaScript in the browser.
 */
public class ExecuteScript extends AbstractDatastarEmitter {

  private final Map<String, String> attributes;
  private final List<String> scripts;
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
    attributes = new HashMap<>();
    scripts = new ArrayList<>();
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
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("name cannot be null or empty");
    }
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("value cannot be null or empty");
    }
    attributes.put(name.trim(), value.trim());
    return this;
  }

  /**
   * Add at least 1 script line. Each script line contains JavaScript to be executed by the browser.
   *
   * @param script the script line
   * @return The ExecuteScript object
   */
  public ExecuteScript script(String script) {
    if (script == null || script.isBlank()) {
      throw new IllegalArgumentException("script is null or empty");
    }
    scripts.add(script);
    return this;
  }

  /** Emit the SSE event */
  public void emit() {
    if (scripts.isEmpty()) {
      throw new IllegalStateException("No scripts specified");
    }
    event.name(DATASTAR_EXECUTE_SCRIPT);
    if (autoRemove != null) {
      event.data(AUTO_REMOVE + autoRemove);
    }
    for (String key : attributes.keySet()) {
      event.data(ATTRIBUTES + key + " " + attributes.get(key));
    }
    for (String script : scripts) {
      event.data(SCRIPT + script);
    }
    emitEvents();
  }
}
