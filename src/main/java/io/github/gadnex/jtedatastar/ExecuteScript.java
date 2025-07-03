package io.github.gadnex.jtedatastar;

import java.util.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar PatchElements events to execute a script on the client.
 *
 * <p>Executes JavaScript in the browser.
 */
public class ExecuteScript extends AbstractDatastarEmitter {

  private final List<String> scripts;
  private Boolean autoRemove;
  private final StringBuilder attributes = new StringBuilder();

  private static final String DATASTAR_PATCH_ELEMENTS = " datastar-patch-elements";
  private static final String SELECTOR = " selector body";
  private static final String ELEMENTS_DATALINE_LITERAL = " elements ";

  /**
   * Constructor for creating the ExecuteScript emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public ExecuteScript(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
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
    attributes.append(" ");
    attributes.append(name);
    if (value != null) {
      attributes.append("=\"");
      attributes.append(value);
      attributes.append("\"");
    }
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
    event.name(DATASTAR_PATCH_ELEMENTS);
    event.data(PatchMode.APPEND.output());
    event.data(SELECTOR);
    if ((autoRemove != null) && autoRemove) {
      event.data(
          ELEMENTS_DATALINE_LITERAL
              + "<script data-effect=\"el.remove()\""
              + attributes.toString()
              + ">");
    } else {
      event.data(ELEMENTS_DATALINE_LITERAL + "<script" + attributes.toString() + ">");
    }
    for (String script : scripts) {
      event.data(ELEMENTS_DATALINE_LITERAL + script);
    }
    event.data(ELEMENTS_DATALINE_LITERAL + "</script>");
    emitEvents();
  }
}
