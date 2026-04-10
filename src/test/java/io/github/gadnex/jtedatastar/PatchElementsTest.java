package io.github.gadnex.jtedatastar;

import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
class PatchElementsTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void patchElements() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    CapturingSseEmitter emitter2 = new CapturingSseEmitter();
    Set<SseEmitter> emitters = Set.of(emitter, emitter2);
    datastar.patchElements(emitters).template("Hello").attribute("name", "John").emit();

    assertThat(emitter.getEmittedData()).contains("data: elements Hello John!");
    assertThat(emitter2.getEmittedData()).contains("data: elements Hello John!");
  }

  @Test
  void selector() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchElements(emitter).selector("#greeting").patchMode(PatchMode.REMOVE).emit();

    assertThat(emitter.getEmittedData())
        .contains("data: mode remove")
        .contains("data: selector #greeting");
  }

  @Test
  void patchModeRemoveMultiple() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchElements(emitter).selector("#feed, #otherid").patchMode(PatchMode.REMOVE).emit();

    assertThat(emitter.getEmittedData())
        .contains("data: mode remove")
        .contains("data: selector #feed, #otherid");
  }

  @Test
  void namespaceSVG() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
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
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar
        .patchElements(emitter)
        .useViewTransition(false)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: useViewTransition false");
  }
}
