package io.github.gadnex.jtedatastar;

import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * A class to emit Datastar MergeFragments events.
 *
 * <p>Merges one or more fragments into the DOM. By default, Datastar merges fragments using
 * Idiomorph, which matches top level elements based on their ID.
 */
public class MergeFragments extends AbstractDatastarEmitter {

  private final TemplateEngine templateEngine;
  private final String templateSuffix;
  private final Map<String, Object> attributes;
  private final MessageSource messageSource;
  private String template;
  private String selector;
  private MergeMode mergeMode;
  private Integer settleDuration;
  private Boolean useViewTransition;

  private static final String LOCALIZER = "localizer";
  private static final String DATASTAR_MERGE_FRAGMENTS = " datastar-merge-fragments";
  private static final String SELECTOR = " selector ";
  private static final String SETTLE_DURATION = " settleDuration ";
  private static final String USE_VIEW_TRANSITION = " useViewTransition ";
  private static final String FRAGMENTS_DATALINE_LITERAL = " fragments ";

  /**
   * Constructor for creating the MergeFragments emitter
   *
   * @param templateEngine The JTE template engine for rendering HTML fragments
   * @param templateSuffix The JTE template suffix for the JTE template files
   * @param sseEmitters The set of SSE emitters to which to emit the events
   * @param messageSource The Spring MessageSource for getting language specific text
   */
  public MergeFragments(
      TemplateEngine templateEngine,
      String templateSuffix,
      Set<SseEmitter> sseEmitters,
      MessageSource messageSource) {
    super(sseEmitters);
    this.templateEngine = templateEngine;
    this.templateSuffix = templateSuffix;
    this.attributes = new HashMap<>();
    this.messageSource = messageSource;
  }

  /**
   * The required template name for HTML fragment rendering
   *
   * @param templateName The name of the JTE template for HTML fragment rendering
   * @return The MergeFragments object
   */
  public MergeFragments template(String templateName) {
    template = templateName + templateSuffix;
    return this;
  }

  /**
   * The required template name for HTML fragment rendering and an optional Locale if localization
   * is required for template rendering.
   *
   * @param templateName The name of the JTE template for HTML fragment rendering
   * @param locale The locale used for localization
   * @return The MergeFragments object
   */
  public MergeFragments template(String templateName, Locale locale) {
    template = templateName + templateSuffix;
    attributes.put(LOCALIZER, new Localizer(messageSource, locale));
    return this;
  }

  /**
   * Add data attribute for template rendering.
   *
   * @param key The key of the attribute used in the template
   * @param value The value of the attribute that will replace the key
   * @return The MergeFragments object
   */
  public MergeFragments attribute(String key, Object value) {
    attributes.put(key, value);
    return this;
  }

  /**
   * Selects the target element of the merge process using a CSS selector.
   *
   * @param selector The CSS selector
   * @return The MergeFragments object
   */
  public MergeFragments selector(String selector) {
    this.selector = selector.trim();
    return this;
  }

  /**
   * Optionally specify the Datastar merge mode. Default is 'morph'.
   *
   * @param mergeMode The merge mode to set
   * @return The MergeFragments object
   */
  public MergeFragments mergeMode(MergeMode mergeMode) {
    this.mergeMode = mergeMode;
    return this;
  }

  /**
   * Optionally settles the element after this time, useful for transitions. Defaults to 300.
   *
   * @param settleDuration The settle duration in ms.
   * @return The MergeFragments object
   */
  public MergeFragments settleDuration(int settleDuration) {
    this.settleDuration = settleDuration;
    return this;
  }

  /**
   * Whether to use view transitions when merging into the DOM. Defaults to false.
   *
   * @param useViewTransition The useViewTransition value
   * @return The MergeFragments object
   */
  public MergeFragments useViewTransition(boolean useViewTransition) {
    this.useViewTransition = useViewTransition;
    return this;
  }

  /** Emit the SSE event */
  public void emit() {
    if (template == null) {
      throw new IllegalStateException("The template must not be null");
    }
    event.name(DATASTAR_MERGE_FRAGMENTS);
    if (selector != null && !selector.isEmpty()) {
      event.data(SELECTOR + selector);
    }
    if (mergeMode != null) {
      event.data(mergeMode.output());
    }
    if (settleDuration != null) {
      event.data(SETTLE_DURATION + settleDuration);
    }
    if (useViewTransition != null) {
      event.data(USE_VIEW_TRANSITION + useViewTransition);
    }
    String html = renderHtmlFragment();
    html.lines()
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .forEach(line -> event.data(FRAGMENTS_DATALINE_LITERAL + line));
    emitEvents();
  }

  private String renderHtmlFragment() {
    var output = new StringOutput();
    templateEngine.render(template, attributes, output);
    return output.toString();
  }
}
