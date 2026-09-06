package io.github.gadnex.jtedatastar.sse;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class PatchSignalsTest implements WithAssertions {

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
  void patchSignals() {
    datastar.patchSignals(emitter).signal("foo", 1).signal("bar", 2).emit();

    assertThat(emitter.getEmittedData())
        .contains("id:")
        .contains("event: datastar-patch-signals")
        .contains("data: signals {\"bar\":2,\"foo\":1}");
  }

  @Test
  void patchSignalsMultipleEmitters() {
    CapturingSseEmitter emitter2 = new CapturingSseEmitter();
    Set<SseEmitter> emitters = Set.of(emitter, emitter2);
    datastar.patchSignals(emitters).signal("foo", 1).signal("bar", 2).emit();

    assertThat(emitter.getEmittedData()).contains("data: signals {\"bar\":2,\"foo\":1}");
    assertThat(emitter2.getEmittedData()).contains("data: signals {\"bar\":2,\"foo\":1}");
  }

  @Test
  void removeSignal() {
    datastar.patchSignals(emitter).signal("key", null).emit();

    assertThat(emitter.getEmittedData()).contains("data: signals {\"key\":null}");
  }

  @Test
  void patchSignalsNested() {
    Map<String, String> user = Map.of("name", "Johnny");
    datastar.patchSignals(emitter).signal("user", user).emit();

    assertThat(emitter.getEmittedData()).contains("data: signals {\"user\":{\"name\":\"Johnny\"}}");
  }

  @Test
  void onlyIfMissingTrue() {
    datastar.patchSignals(emitter).onlyIfMissing(true).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData()).contains("onlyIfMissing true");
  }

  @Test
  void onlyIfMissingFalse() {
    datastar.patchSignals(emitter).onlyIfMissing(false).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData()).contains("onlyIfMissing false");
  }
}
