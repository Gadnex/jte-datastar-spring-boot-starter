package io.github.gadnex.jtedatastar;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExecuteScriptTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void executeScript() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.executeScript(emitter).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData())
        .contains("id:")
        .contains("event: datastar-patch-elements")
        .contains("data: mode append")
        .contains("data: selector body")
        .contains("data: elements <script>")
        .contains("data: elements alert('Hello World!');")
        .contains("data: elements </script>");
  }

  @Test
  void scriptAttribute() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar
        .executeScript(emitter)
        .attribute("referrerpolicy", "origin")
        .script("alert('Hello World!');")
        .emit();

    assertThat(emitter.getEmittedData())
        .contains("data: elements <script referrerpolicy=\"origin\">");
  }

  @Test
  void scriptAttributeWithNullValue() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar
        .executeScript(emitter)
        .attribute("defer", null)
        .script("alert('Hello World!');")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: elements <script defer>");
  }

  @Test
  void autoRemoveTrue() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.executeScript(emitter).autoRemove(true).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData())
        .contains("data: elements <script data-effect=\"el.remove()\">");
  }

  @Test
  void autoRemoveFalse() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.executeScript(emitter).autoRemove(false).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData()).contains("data: elements <script>");
  }
}
