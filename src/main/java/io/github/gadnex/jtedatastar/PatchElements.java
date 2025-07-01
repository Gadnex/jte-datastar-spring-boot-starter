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
 * A class to emit Datastar PatchElements events.
 *
 * <p>Patches one or more elements into the DOM. By default, Datastar patches elements using
 * Idiomorph, which matches top level elements based on their ID. It can also remove elements from
 * the DOM.
 */
public class PatchElements extends AbstractDatastarEmitter {

  private final TemplateEngine templateEngine;
  private final String templateSuffix;
  private final Map<String, Object> attributes;
  private final MessageSource messageSource;
  private String template;
  private String selector;
  private PatchMode patchMode;
  private Boolean useViewTransition;

  private static final String LOCALIZER = "localizer";
  private static final String DATASTAR_PATCH_ELEMENTS = " datastar-patch-elements";
  private static final String SELECTOR = " selector ";
  private static final String USE_VIEW_TRANSITION = " useViewTransition ";
  private static final String ELEMENTS_DATALINE_LITERAL = " elements ";

  /**
   * Constructor for creating the PatchElements emitter
   *
   * @param templateEngine The JTE template engine for rendering HTML elements
   * @param templateSuffix The JTE template suffix for the JTE template files
   * @param sseEmitters The set of SSE emitters to which to emit the events
   * @param messageSource The Spring MessageSource for getting language specific text
   */
  public PatchElements(
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
   * The required template name for HTML element rendering
   *
   * @param templateName The name of the JTE template for HTML element rendering
   * @return The PatchElements object
   */
  public PatchElements template(String templateName) {
    template = templateName + templateSuffix;
    return this;
  }

  /**
   * The required template name for HTML element rendering and an optional Locale if localization is
   * required for template rendering.
   *
   * @param templateName The name of the JTE template for HTML element rendering
   * @param locale The locale used for localization
   * @return The PatchElements object
   */
  public PatchElements template(String templateName, Locale locale) {
    template = templateName + templateSuffix;
    attributes.put(LOCALIZER, new Localizer(messageSource, locale));
    return this;
  }

  /**
   * Add data attribute for template rendering.
   *
   * @param key The key of the attribute used in the template
   * @param value The value of the attribute that will replace the key
   * @return The PatchElements object
   */
  public PatchElements attribute(String key, Object value) {
    attributes.put(key, value);
    return this;
  }

  /**
   * Selects the target element of the patch process using a CSS selector. Multiple selectors can be
   * added as a comma separated list.
   *
   * @param selector The CSS selector
   * @return The PatchElements object
   */
  public PatchElements selector(String selector) {
    this.selector = selector.trim();
    return this;
  }

  /**
   * Optionally specify the Datastar patch mode. Default is 'outer'.
   *
   * @param patchMode The patch mode to set
   * @return The PatchElements object
   */
  public PatchElements patchMode(PatchMode patchMode) {
    this.patchMode = patchMode;
    return this;
  }

  /**
   * Whether to use view transitions when merging into the DOM. Defaults to false.
   *
   * @param useViewTransition The useViewTransition value
   * @return The PatchElements object
   */
  public PatchElements useViewTransition(boolean useViewTransition) {
    this.useViewTransition = useViewTransition;
    return this;
  }

  /** Emit the SSE event */
  public void emit() {
    if ((patchMode != PatchMode.REMOVE) && (template == null)) {
      throw new IllegalStateException("The template must not be null");
    }
    event.name(DATASTAR_PATCH_ELEMENTS);
    if (patchMode != null) {
      event.data(patchMode.output());
    }
    if (selector != null && !selector.isEmpty()) {
      event.data(SELECTOR + selector);
    }
    if (useViewTransition != null) {
      event.data(USE_VIEW_TRANSITION + useViewTransition);
    }
    if (template != null) {
      String html = renderHtmlElement();
      html.lines()
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .forEach(line -> event.data(ELEMENTS_DATALINE_LITERAL + line));
    }
    emitEvents();
  }

  private String renderHtmlElement() {
    var output = new StringOutput();
    templateEngine.render(template, attributes, output);
    return output.toString();
  }
}
