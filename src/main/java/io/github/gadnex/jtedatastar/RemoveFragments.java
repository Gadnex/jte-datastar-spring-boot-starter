package io.github.gadnex.jtedatastar;

import java.util.HashSet;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar RemoveFragments events.
 *
 * <p>Removes one or more HTML fragments that match the provided selector from the DOM.
 */
public class RemoveFragments extends AbstractDatastarEmitter {

  private final Set<String> selectors;
  private static final String DATASTAR_REMOVE_FRAGMENTS = " datastar-remove-fragments";
  private static final String SELECTOR = " selector ";

  /**
   * Constructor for creating the RemoveFragments emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public RemoveFragments(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
    this.selectors = new HashSet<>();
  }

  /**
   * Selects the HTML target element to remove using a CSS selector.
   *
   * @param selector The CSS selector
   * @return The RemoveFragments object
   */
  public RemoveFragments selector(String selector) {
    selectors.add(selector);
    return this;
  }

  /**
   * Emit the SSE event
   *
   * @throws EmitException Exception if the event fails to be emitted
   */
  public void emit() throws EmitException {
    if (selectors.isEmpty()) {
      throw new IllegalStateException("No selectors specified");
    }
    event.name(DATASTAR_REMOVE_FRAGMENTS);
    for (String selector : selectors) {
      event.data(SELECTOR + selector);
    }
    emitEvents();
  }
}
