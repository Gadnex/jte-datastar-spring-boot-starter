package io.github.gadnex.jtedatastar;

import java.util.Map;
import java.util.Set;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
class PatchSignalsTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void patchSignals() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).signal("foo", 1).signal("bar", 2).emit();

    assertThat(emitter.getEmittedData())
        .contains("id:")
        .contains("event: datastar-patch-signals")
        .contains("data: signals {\"bar\":2,\"foo\":1}");
  }

  @Test
  void patchSignalsMultipleEmitters() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    CapturingSseEmitter emitter2 = new CapturingSseEmitter();
    Set<SseEmitter> emitters = Set.of(emitter, emitter2);
    datastar.patchSignals(emitters).signal("foo", 1).signal("bar", 2).emit();

    assertThat(emitter.getEmittedData()).contains("data: signals {\"bar\":2,\"foo\":1}");
    assertThat(emitter2.getEmittedData()).contains("data: signals {\"bar\":2,\"foo\":1}");
  }

  @Test
  void patchSignalsNested() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    Map<String, String> user = Map.of("name", "Johnny");
    datastar.patchSignals(emitter).signal("user", user).emit();

    assertThat(emitter.getEmittedData()).contains("data: signals {\"user\":{\"name\":\"Johnny\"}}");
  }

  @Test
  void onlyIfMissingTrue() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).onlyIfMissing(true).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData()).contains("onlyIfMissing true");
  }

  @Test
  void onlyIfMissingFalse() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).onlyIfMissing(false).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData()).contains("onlyIfMissing false");
  }
}
