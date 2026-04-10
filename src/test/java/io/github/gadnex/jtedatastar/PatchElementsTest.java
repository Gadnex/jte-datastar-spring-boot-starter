package io.github.gadnex.jtedatastar;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PatchElementsTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void patchElements() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchElements(emitter).template("Hello").attribute("name", "John").emit();

    assertThat(emitter.getEmittedData()).hasSize(7);
    assertThat(emitter.getEmittedData().get(3)).contains("Hello John!");
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

    assertThat(emitter.getEmittedData().get(1)).contains("selector #foo");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode outer");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode inner");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode replace");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode prepend");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode append");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode before");
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

    assertThat(emitter.getEmittedData().get(1)).contains("mode after");
  }

  @Test
  void patchModeRemove() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar
        .patchElements(emitter)
        .patchMode(PatchMode.REMOVE)
        .template("Hello")
        .attribute("name", "John")
        .emit();

    assertThat(emitter.getEmittedData().get(1)).contains("mode remove");
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

    assertThat(emitter.getEmittedData().get(1)).contains("namespace svg");
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

    assertThat(emitter.getEmittedData().get(1)).contains("namespace mathml");
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

    assertThat(emitter.getEmittedData().get(1)).contains("useViewTransition true");
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

    assertThat(emitter.getEmittedData().get(1)).contains("useViewTransition false");
  }

  @Test
  void elements() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchElements(emitter).template("Hello").attribute("name", "John").emit();

    assertThat(emitter.getEmittedData().get(1)).contains("elements");
  }
}
