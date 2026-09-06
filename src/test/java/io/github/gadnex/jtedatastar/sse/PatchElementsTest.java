package io.github.gadnex.jtedatastar.sse;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import java.nio.file.Path;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class PatchElementsTest implements WithAssertions {

  private static Datastar datastar;
  private CapturingSseEmitter emitter;

  @BeforeAll
  static void initDatastar() {
    TemplateEngine templateEngine =
        TemplateEngine.create(new DirectoryCodeResolver(Path.of("src/main/jte")), ContentType.Html);
    MessageSource messageSource = Mockito.mock(MessageSource.class);
    datastar = new Datastar(templateEngine, ".jte", messageSource);
  }

  @BeforeEach
  void setUp() {
    emitter = new CapturingSseEmitter();
  }

  @Test
  void patchElements() {
    datastar.patchElements(emitter).template("Hello").attribute("name", "John").emit();

    assertThat(emitter.getEmittedData())
        .contains("id:")
        .contains("event: datastar-patch-elements")
        .contains("data: elements <div id=\"greeting\">")
        .contains("data: elements Hello John!")
        .contains("data: elements </div>");
  }

  @Test
  void patchElementsMultipleEmitters() {
    CapturingSseEmitter emitter2 = new CapturingSseEmitter();
    Set<SseEmitter> emitters = Set.of(emitter, emitter2);
    datastar.patchElements(emitters).template("Hello").attribute("name", "John").emit();

    assertThat(emitter.getEmittedData()).contains("data: elements Hello John!");
    assertThat(emitter2.getEmittedData()).contains("data: elements Hello John!");
  }

  @Test
  void selector() {
    datastar
        .patchElements(emitter)
        .selector("#foo")
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: selector #foo");
  }

  @Test
  void patchModeOuter() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.OUTER)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode outer");
  }

  @Test
  void patchModeInner() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.INNER)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode inner");
  }

  @Test
  void patchModeReplace() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.REPLACE)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode replace");
  }

  @Test
  void patchModePrepend() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.PREPEND)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode prepend");
  }

  @Test
  void patchModeAppend() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.APPEND)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode append");
  }

  @Test
  void patchModeBefore() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.BEFORE)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode before");
  }

  @Test
  void patchModeAfter() {
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.AFTER)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: mode after");
  }

  @Test
  void patchModeRemove() {
    datastar.patchElements(emitter).selector("#greeting").patchMode(PatchMode.REMOVE).emit();

    assertThat(emitter.getEmittedData())
        .contains("data: mode remove")
        .contains("data: selector #greeting");
  }

  @Test
  void patchModeRemoveMultiple() {
    datastar.patchElements(emitter).selector("#feed, #otherid").patchMode(PatchMode.REMOVE).emit();

    assertThat(emitter.getEmittedData())
        .contains("data: mode remove")
        .contains("data: selector #feed, #otherid");
  }

  @Test
  void namespaceHTML() {
    datastar
        .patchElements(emitter)
        .namespace(Namespace.HTML)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: namespace html");
  }

  @Test
  void namespaceSVG() {
    datastar
        .patchElements(emitter)
        .namespace(Namespace.SVG)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: namespace svg");
  }

  @Test
  void namespaceMathML() {
    datastar
        .patchElements(emitter)
        .namespace(Namespace.MATHML)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: namespace mathml");
  }

  @Test
  void useViewTransitionTrue() {
    datastar
        .patchElements(emitter)
        .useViewTransition(true)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: useViewTransition true");
  }

  @Test
  void useViewTransitionFalse() {
    datastar
        .patchElements(emitter)
        .useViewTransition(false)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: useViewTransition false");
  }

  @Test
  void viewTransitionSelector() {
    datastar
        .patchElements(emitter)
        .viewTransitionSelector("#mySelector")
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: viewTransitionSelector #mySelector");
  }
}
