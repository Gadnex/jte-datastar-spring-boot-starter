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

    System.out.println(emitter.getEmittedData());

    assertThat(emitter.getEmittedData()).hasSize(7);
    assertThat(emitter.getEmittedData().get(3)).contains("Hello John!");
  }
}
