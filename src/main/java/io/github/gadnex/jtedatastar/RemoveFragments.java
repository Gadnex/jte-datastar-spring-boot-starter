package io.github.gadnex.jtedatastar;

import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar RemoveFragments events.
 *
 * <p>Removes one or more HTML fragments that match the provided selector from the DOM.
 */
public class RemoveFragments extends AbstractDatastarEmitter {

  private String selector;
  private Integer settleDuration;
  private Boolean useViewTransition;
  private static final String DATASTAR_REMOVE_FRAGMENTS = " datastar-remove-fragments";
  private static final String SELECTOR = " selector ";
  private static final String SETTLE_DURATION = " settleDuration ";
  private static final String USE_VIEW_TRANSITION = " useViewTransition ";

  /**
   * Constructor for creating the RemoveFragments emitter
   *
   * @param sseEmitters The set of SSE emitters to which to emit the events
   */
  public RemoveFragments(Set<SseEmitter> sseEmitters) {
    super(sseEmitters);
  }

  /**
   * Selects the HTML target element to remove using a CSS selector.
   *
   * @param selector The CSS selector
   * @return The RemoveFragments object
   */
  public RemoveFragments selector(String selector) {
    this.selector = selector.trim();
    return this;
  }

  /**
   * Optionally settles the element after this time, useful for transitions. Defaults to 300.
   *
   * @param settleDuration The settle duration in ms.
   * @return The RemoveFragments object
   */
  public RemoveFragments settleDuration(int settleDuration) {
    this.settleDuration = settleDuration;
    return this;
  }

  /**
   * Whether to use view transitions when merging into the DOM. Defaults to false.
   *
   * @param useViewTransition The useViewTransition value
   * @return The RemoveFragments object
   */
  public RemoveFragments useViewTransition(boolean useViewTransition) {
    this.useViewTransition = useViewTransition;
    return this;
  }

  /**
   * Emit the SSE event
   *
   * @throws EmitException Exception if the event fails to be emitted
   */
  public void emit() throws EmitException {
    if (selector == null || selector.isBlank()) {
      throw new IllegalStateException("No selector specified");
    }
    event.name(DATASTAR_REMOVE_FRAGMENTS);
    event.data(SELECTOR + selector);
    if (settleDuration != null) {
      event.data(SETTLE_DURATION + settleDuration);
    }
    if (useViewTransition != null) {
      event.data(USE_VIEW_TRANSITION + useViewTransition);
    }
    emitEvents();
  }
}
