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

    assertThat(emitter.getEmittedData())
        .contains("id:")
        .contains("event: datastar-patch-signals")
        .contains("data: signals {\"bar\":2,\"foo\":1}");
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
