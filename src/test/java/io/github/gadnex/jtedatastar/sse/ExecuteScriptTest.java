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

class ExecuteScriptTest implements WithAssertions {

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
  void executeScript() {
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
  void executeScriptMultipleEmitters() {
    CapturingSseEmitter emitter2 = new CapturingSseEmitter();
    Set<SseEmitter> emitters = Set.of(emitter, emitter2);
    datastar.executeScript(emitters).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData()).contains("data: elements alert('Hello World!');");
    assertThat(emitter2.getEmittedData()).contains("data: elements alert('Hello World!');");
  }

  @Test
  void scriptAttribute() {
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
    datastar
        .executeScript(emitter)
        .attribute("defer", null)
        .script("alert('Hello World!');")
        .emit();

    assertThat(emitter.getEmittedData()).contains("data: elements <script defer>");
  }

  @Test
  void autoRemoveTrue() {
    datastar.executeScript(emitter).autoRemove(true).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData())
        .contains("data: elements <script data-effect=\"el.remove()\">");
  }

  @Test
  void autoRemoveFalse() {
    datastar.executeScript(emitter).autoRemove(false).script("alert('Hello World!');").emit();

    assertThat(emitter.getEmittedData()).contains("data: elements <script>");
  }
}
