package io.github.gadnex.jtedatastar;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PatchSignalsTest implements WithAssertions {

  @Autowired private Datastar datastar;

  @Test
  void patchSignals() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).signal("foo", 1).signal("bar", 2).emit();

    assertThat(emitter.getEmittedData()).hasSize(3);
    assertThat(emitter.getEmittedData().get(1)).contains("{\"bar\":2,\"foo\":1}");
  }

  @Test
  void eventType() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData().get(0)).contains("event: datastar-patch-signals");
  }

  @Test
  void signals() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData().get(1)).contains("signals");
  }

  @Test
  void onlyIfMissingTrue() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).onlyIfMissing(true).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData().get(1)).contains("onlyIfMissing true");
  }

  @Test
  void onlyIfMissingFalse() {
    CapturingSseEmitter emitter = new CapturingSseEmitter();
    datastar.patchSignals(emitter).onlyIfMissing(false).signal("foo", 1).emit();

    assertThat(emitter.getEmittedData().get(1)).contains("onlyIfMissing false");
  }
}
